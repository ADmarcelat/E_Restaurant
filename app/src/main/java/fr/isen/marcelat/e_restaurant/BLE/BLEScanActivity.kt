package fr.isen.marcelat.e_restaurant.BLE

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.isen.marcelat.e_restaurant.R
import fr.isen.marcelat.e_restaurant.databinding.ActivityBlescanBinding

class BLEScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlescanBinding
    private var isScanning = false


    private var adapter: BLEAdapter?=null

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBlescanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Equivalent switch
        when{
            bluetoothAdapter?.isEnabled == true ->
                binding.bleScanStateImg.setOnClickListener {
                    startLeScanBLEWithPermission(!isScanning)
                }
            bluetoothAdapter != null ->
                askBluetoothPermission()
            else -> {
                displayBleUnavailable()
            }
        }

        adapter = BLEAdapter(arrayListOf()) {
            val intent = Intent(this, BLEDeviceActivity::class.java)
            intent.putExtra(DEVICE_KEY, it)
            startActivity(intent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onStop() {
        super.onStop()
        startLeScanBLEWithPermission(false)
    }

    private fun startLeScanBLEWithPermission(enable: Boolean){
        if(ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION //Verifie que l'utilisateur a deja validé la geoloc
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startLeScanBLE(enable) //Je demarre le ble

        }else {//Sinon
            ActivityCompat.requestPermissions(this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,//Je fais la demande
                //Pour Android 12
                //android.Manifest.permission.BLUETOOTH_CONNECT,
                // android.Manifest.permission.BLUETOOTH_SCAN,
            ),ALL_PERMISSION_REQUEST_CODE)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLeScanBLE(enable: Boolean) {

        bluetoothAdapter?.bluetoothLeScanner?.apply {
            if (enable) {
                isScanning = true
                startScan(scanCallback)//startScan viens de "bluetoothLeScanner
            } else {
                isScanning = false
                stopScan(scanCallback)

            }
            handlePlayPauseAction()
        }
    }

    //Retour asynchrone appellée de manière assez regulière
    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            (binding.recyclerView.adapter as BLEAdapter).apply{
                fillBLE(result)
                notifyDataSetChanged()
            }
        }
    }

    private fun askBluetoothPermission() {
        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        )
        {
            startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH_REQUEST_CODE)
        }
    }

    private fun displayBleUnavailable(){
        binding.bleScanStateImg.isVisible = false
        binding.BleTitle.text = getString(R.string.ble_scan_device_error)
        binding.LoadBar.isIndeterminate = false
    }

    private fun handlePlayPauseAction() {
        if(isScanning){
            binding.bleScanStateImg.setImageResource(R.drawable.ic_pause)
            binding.BleTitle.text = getString(R.string.ble_scan_pause)
            binding.LoadBar.isIndeterminate = true
            binding.LoadBar.isVisible = true

        } else {
            binding.bleScanStateImg.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            binding.BleTitle.text = getString(R.string.ble_scan_play)
            binding.LoadBar.isIndeterminate = false
            binding.LoadBar.isVisible = false
        }
    }

    companion object {
        private const val ENABLE_BLUETOOTH_REQUEST_CODE = 1
        private const val ALL_PERMISSION_REQUEST_CODE = 100
         const val DEVICE_KEY = "device"
    }

}