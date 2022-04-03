package fr.isen.marcelat.e_restaurant.BLE

import android.bluetooth.BluetoothGattCharacteristic
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup


class BleService(val name: String, val characteristics: MutableList<BluetoothGattCharacteristic>):
        ExpandableGroup<BluetoothGattCharacteristic>(name, characteristics){

        }


