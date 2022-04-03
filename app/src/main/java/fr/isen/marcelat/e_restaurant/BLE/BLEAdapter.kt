package fr.isen.marcelat.e_restaurant.BLE


import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.marcelat.e_restaurant.R


internal class BLEAdapter(private val bleList: ArrayList<ScanResult>, val clickListener: (BluetoothDevice) -> Unit) :
    RecyclerView.Adapter<BLEAdapter.BLEViewHolder>() {

    internal inner class BLEViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var MAC_Address: TextView = view.findViewById(R.id.BLE_Mac)
        val rssi: TextView = view.findViewById(R.id.BLE_db)
        var BLEname: TextView = view.findViewById(R.id.BLEName)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BLEViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_ble, parent, false)
        return BLEViewHolder(itemView)
    }

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: BLEViewHolder, position: Int) {
        val ble = bleList[position]

        holder.MAC_Address.text = ble.device.address
        holder.rssi.text = "\n" + ble.rssi.toString()
        holder.BLEname.text = ble.device.name

        holder.itemView.setOnClickListener{
            clickListener(ble.device)
        }
    }

    fun fillBLE(scanResult: ScanResult){
        val index: Int = bleList.indexOfFirst{it.device.address == scanResult.device.address}
        if(bleList.indexOf(scanResult) == -1){
            bleList.add(scanResult)
            notifyItemChanged(index)
        }
        else{
            bleList[index] = scanResult
            notifyItemInserted(bleList.size -1)
        }
            bleList.sortByDescending{ it.rssi }
    }




    override fun getItemCount(): Int {
        return bleList.size
    }


}