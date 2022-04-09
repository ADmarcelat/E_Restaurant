package fr.isen.marcelat.e_restaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fr.isen.marcelat.e_restaurant.BLE.BLEScanActivity
import fr.isen.marcelat.e_restaurant.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.entrees.setOnClickListener {
            goToMenus("Entrees")
        }

        binding.plats.setOnClickListener{
            goToMenus("Plats")
        }

        binding.desserts.setOnClickListener {
            goToMenus("Desserts")
        }

        binding.buttonBLE.setOnClickListener {
            val intent = Intent(this, BLEScanActivity::class.java)
            startActivity(intent)
        }

        binding.basket.setOnClickListener{
            val intent = Intent(this, PanierActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.d("TAG","HomeActivity")
    }

    private fun goToMenus(category: String){
        val intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("category" , category)
        startActivity(intent)
        }
    }




