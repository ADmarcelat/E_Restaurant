package fr.isen.marcelat.e_restaurant.BLE

import BLEService
import android.annotation.SuppressLint
import android.bluetooth.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import fr.isen.marcelat.e_restaurant.R
import fr.isen.marcelat.e_restaurant.databinding.ActivityBleDeviceBinding


class BLEDeviceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBleDeviceBinding
    private var bluetoothGatt: BluetoothGatt? = null
    private lateinit var adapter: BLEServiceAdapter

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBleDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val device = intent.getParcelableExtra<BluetoothDevice?>("Device")
        binding.deviceName.text = device?.name ?: getString(R.string.ble_unknown)
        binding.deviceStatus.text = getString(R.string.ble_disconnected)

        connectToDevice(device)
    }

    override fun onStop() {
        super.onStop()
        closeBluetoothGatt()
    }

    @SuppressLint("MissingPermission")
    private fun closeBluetoothGatt() {
        bluetoothGatt?.close()
        bluetoothGatt=null
    }

    @SuppressLint("MissingPermission")
    private fun connectToDevice(device: BluetoothDevice?) {
        this.bluetoothGatt= device?.connectGatt(this, true, gattCallback)
        this.bluetoothGatt?.connect()
    }

    private val gattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothGatt.STATE_CONNECTED -> {
                    gatt?.discoverServices()
                    runOnUiThread {  binding.deviceStatus.text = getString(R.string.ble_connected)}
                }
                BluetoothGatt.STATE_CONNECTING -> { runOnUiThread {  binding.deviceStatus.text = getString(R.string.ble_connecting)} }
                else -> {runOnUiThread {  binding.deviceStatus.text = getString(R.string.ble_disconnected)}
                }
            }
        }

        @SuppressLint("MissingPermission")
        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)

            val bleServices=gatt?.services?.map{BLEService(it.uuid.toString(),it.characteristics)} ?: arrayListOf()
            runOnUiThread {
                binding.serviceList.layoutManager=LinearLayoutManager(this@BLEDeviceActivity)


                adapter= BLEServiceAdapter(bleServices,
                    { characteristic -> gatt?.readCharacteristic(characteristic) },
                    { characteristic -> writeIntoCharacteristic(gatt!!, characteristic) },
                )
                binding.serviceList.adapter=adapter
            }




        }
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) { super.onCharacteristicRead(gatt, characteristic, status)
            runOnUiThread {
                adapter.updateFromChangedCharacteristic(characteristic)
                adapter.notifyDataSetChanged()
            }
        }
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int
        ) { super.onCharacteristicWrite(gatt, characteristic, status)}
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?
        ) {super.onCharacteristicChanged(gatt, characteristic) }





        @SuppressLint("MissingPermission")
        private fun writeIntoCharacteristic(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic
        ) {
            runOnUiThread {
                val input = EditText(this@BLEDeviceActivity)
                input.inputType = InputType.TYPE_CLASS_TEXT
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(16, 0, 16, 0)
                input.layoutParams = params

                AlertDialog.Builder(this@BLEDeviceActivity)
                    .setTitle(R.string.valeur)
                    .setView(input)
                    .setPositiveButton(R.string.valider) { _, _ ->
                        characteristic.value = input.text.toString().toByteArray()
                        gatt.writeCharacteristic(characteristic)
                        gatt.readCharacteristic(characteristic)
                    }
                    .setNegativeButton(R.string.refuser) { dialog, _ -> dialog.cancel() }
                    .create()
                    .show()
            }
        }
    }

}









