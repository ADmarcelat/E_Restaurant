package fr.isen.marcelat.e_restaurant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.google.gson.Gson

import fr.isen.marcelat.e_restaurant.databinding.ActivityPanierBinding
import fr.isen.marcelat.e_restaurant.model.ItemPanier
import fr.isen.marcelat.e_restaurant.model.PanierList
import java.io.File

class PanierActivity : AppCompatActivity() {
    private lateinit var Recycler: RecyclerView
    private lateinit var binding: ActivityPanierBinding
    private lateinit var panierList: PanierList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPanierBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val file = File(cacheDir.absolutePath + "panier.json")

        panierList = if (file.exists()) {
            Gson().fromJson(file.readText(), PanierList::class.java)
        } else {
            PanierList(arrayListOf())
        }


        Recycler = binding.panierRecyclerView
        Recycler.layoutManager = LinearLayoutManager(this)
        Recycler.adapter = PanierAdapter(panierList.info) {
            delete(it)
        }

        binding.commandeButton.setOnClickListener {
            order()
        }
    }

    private fun order() {
        panierList = PanierList(arrayListOf())
        val strPanier = Gson().toJson(panierList, PanierList::class.java)
        File(cacheDir.absolutePath + "panier.json").writeText(strPanier)


        val intent = Intent(this, HomeActivity::class.java)
        Toast.makeText(applicationContext, R.string.merci, Toast.LENGTH_SHORT).show()
        startActivity(intent)

    }

    private fun delete(item: ItemPanier) {
        if (panierList.info.size == 0) {
            panierList = PanierList(arrayListOf())
        } else {
            panierList.info.forEach {
                if (it.item.equals(item)) {
                    panierList.info.remove(it)
                }
            }
        }
        val strPanier = Gson().toJson(panierList, PanierList::class.java)
        File(cacheDir.absolutePath + "panier.json").writeText(strPanier)
    }
}