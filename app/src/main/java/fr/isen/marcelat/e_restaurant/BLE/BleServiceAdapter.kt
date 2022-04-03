package fr.isen.marcelat.e_restaurant.BLE

import android.app.Service
import android.bluetooth.BluetoothGattCharacteristic
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import fr.isen.marcelat.e_restaurant.R

class BleServiceAdapter(bleServices: List<BleService>) :
    ExpandableRecyclerViewAdapter<BleServiceAdapter.ServiceViewHolder,  BleServiceAdapter.CharacteristicViewHolder>(bleServices) {

    class ServiceViewHolder(itemView: View): GroupViewHolder(itemView) {
        var itemName: TextView = itemView.findViewById(R.id.ServiceName)
        var itemLogo: TextView = itemView.findViewById(R.id.serviceUUID)
        var serviceArrow: ImageView = itemView.findViewById(R.id.serviceList)
    }

    class CharacteristicViewHolder(itemView: View): ChildViewHolder(itemView) {
        var characteristicName: TextView = itemView.findViewById(R.id.characteristicName)
        var characteristicUUID: TextView = itemView.findViewById(R.id.characteristicUUID)
        var readAction : Button = itemView.findViewById(R.id.ReadAction)
        var writeAction: Button = itemView.findViewById(R.id.writeAction)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_ble_service, parent, false)

        return ServiceViewHolder(itemView)
    }

    override fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CharacteristicViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.cell_ble_characteristic, parent,false)

        return CharacteristicViewHolder(itemView)
    }

    override fun onBindChildViewHolder(
        holder: CharacteristicViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,
        childIndex: Int
    ) {
        val characteristic = group.items[childIndex] as BluetoothGattCharacteristic
        holder.characteristicUUID.text = characteristic.uuid.toString()
    }

    override fun onBindGroupViewHolder(
        holder: ServiceViewHolder,
        flatPosition: Int,
        group: ExpandableGroup<*>,

    ) {
        holder.itemName.text

    }
}
