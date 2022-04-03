package fr.isen.marcelat.e_restaurant.BLE

import android.annotation.SuppressLint
import android.bluetooth.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.marcelat.e_restaurant.R
import fr.isen.marcelat.e_restaurant.databinding.ActivityBleDeviceBinding

@SuppressLint("MissingPermission")
class BLEDeviceActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBleDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBleDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<BluetoothDevice?>(BLEScanActivity.DEVICE_KEY)
        binding.deviceName.text = device?.name ?: "Nom inconnu"
        binding.deviceStatus.text = getString(R.string.ble_device_disconnected)

        connectToDevice(device)
    }

    private fun connectToDevice(device: BluetoothDevice?) {
       bluetoothGatt = device?.connectGatt(this, true, object:BluetoothGattCallback() {
           override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
               super.onConnectionStateChange(gatt, status, newState)
               connectionStateChange(gatt, newState)
           }

           override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
               super.onServicesDiscovered(gatt, status)
               val bleServices =
                   gatt?.services?.map{BleService(it.uuid.toString(), it.characteristics)}
                       ?: listOf()
               val adapter = BleServiceAdapter(bleServices as MutableList<BleService>)
               runOnUiThread{
                   binding.serviceList.layoutManager = LinearLayoutManager(this@BLEDeviceActivity)
                   binding.serviceList.adapter = adapter as RecyclerView.Adapter<*>
               }
           }

           override fun onCharacteristicRead(
               gatt: BluetoothGatt?,
               characteristic: BluetoothGattCharacteristic?,
               status: Int
           ) {
               super.onCharacteristicRead(gatt, characteristic, status)
           }
       })
        bluetoothGatt?.connect()
    }

    private fun connectionStateChange(gatt : BluetoothGatt?, newState: Int){
        val state = if(newState == BluetoothProfile.STATE_CONNECTED) {
            gatt?.discoverServices()
            getString(R.string.ble_device_connected)
        } else {
            getString(R.string.ble_device_disconnected)
        }
        runOnUiThread{
            binding.deviceStatus.text = state
        }
    }

    override fun onStop() {
        super.onStop()
        closeBluetoothGatt()
    }

    private fun closeBluetoothGatt(){
        bluetoothGatt?.close()
        bluetoothGatt = null
    }
}