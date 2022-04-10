package fr.isen.marcelat.e_restaurant.BLE

import BLEService
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.marcelat.e_restaurant.R
import java.util.*

class BLEServiceAdapter (private val bleServices:List<BLEService>,
                         private val readCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
                         private val writeCharacteristicCallback: (BluetoothGattCharacteristic) -> Unit,
) : ExpandableRecyclerViewAdapter<BLEServiceAdapter.ServiceViewHolder, BLEServiceAdapter.CharacteristicViewHolder>(bleServices) {

    class ServiceViewHolder(itemView: View):GroupViewHolder(itemView) {
        var serviceName: TextView = itemView.findViewById(R.id.ServiceName)
        var serviceUuid: TextView = itemView.findViewById(R.id.serviceUUID)
        private val serviceArrow: View = itemView.findViewById(R.id.downArrow)
    }

    class CharacteristicViewHolder(itemView: View): ChildViewHolder(itemView){

        var characteristicName: TextView = itemView.findViewById(R.id.characteristicName)
        var characteristicUuid: TextView = itemView.findViewById(R.id.characteristicUUID)
        var characteristicProperties: TextView = itemView.findViewById(R.id.characteristicProperties)
        var characteristicValue: TextView = itemView.findViewById(R.id.characteristicValues)

        var characteristicReadAction: Button = itemView.findViewById(R.id.ReadAction)
        var characteristicWriteAction: Button = itemView.findViewById(R.id.WriteAction)

    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder =
        ServiceViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_ble_service, parent, false)
        )
    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): CharacteristicViewHolder =
        CharacteristicViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.cell_ble_characteristic, parent, false)
        )

    override fun onBindChildViewHolder(holder: CharacteristicViewHolder, flatPosition: Int, group: ExpandableGroup<*>, childIndex: Int) {
        val characteristic = group.items?.get(childIndex) as BluetoothGattCharacteristic
        val title = BLEUUIDAttributes.getBLEAttributeFromUUID(characteristic.uuid.toString()).title
        val uuidMessage = "UUID : ${characteristic.uuid}"
        holder.characteristicUuid.text = uuidMessage
        holder.characteristicName.text = title

        val properties = arrayListOf<String>()
        addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Lecture",
            BluetoothGattCharacteristic.PROPERTY_READ,
            holder.characteristicReadAction,
            readCharacteristicCallback
        )
        addPropertyFromCharacteristic(
            characteristic,
            properties,
            "Ecrire",
            (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE),
            holder.characteristicWriteAction,
            writeCharacteristicCallback
        )

        val proprietiesMessage = "Proprietes : ${properties.joinToString()}"
        holder.characteristicProperties.text = proprietiesMessage

        characteristic.value?.let {
            val hex = it.joinToString("-") { byte -> "%02x".format(byte)}.uppercase(Locale.FRANCE)
            val value = "Valeur : ${String(it)} Hex : 0x$hex"
            holder.characteristicValue.visibility = View.VISIBLE
            holder.characteristicValue.text = value
        }
    }

    override fun onBindGroupViewHolder(holder: ServiceViewHolder, flatPosition: Int, group: ExpandableGroup<*>) {
        val title = BLEUUIDAttributes.getBLEAttributeFromUUID(group.title).title
        holder.serviceName.text=title
        holder.serviceUuid.text=group.title
    }

    fun updateFromChangedCharacteristic(characteristic: BluetoothGattCharacteristic?) {
        this.bleServices.forEach {
            val index = it.items.indexOf(characteristic)
            if(index != -1) {
                it.items.removeAt(index)
                it.items.add(index, characteristic)
            }
        }
    }
    private fun addPropertyFromCharacteristic(
        characteristic: BluetoothGattCharacteristic,
        properties: ArrayList<String>,
        propertyName: String,
        propertyType: Int,
        propertyAction: Button,
        propertyCallBack: (BluetoothGattCharacteristic) -> Unit
    ) {
        if ((characteristic.properties and propertyType) != 0) {
            properties.add(propertyName)
            propertyAction.isEnabled = true
            propertyAction.alpha = 1f
            propertyAction.setOnClickListener {
                propertyCallBack.invoke(characteristic)
            }
        }
    }
}
