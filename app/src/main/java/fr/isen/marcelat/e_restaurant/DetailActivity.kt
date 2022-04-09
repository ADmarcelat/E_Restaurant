package fr.isen.marcelat.e_restaurant


import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.appcompat.app.AlertDialog

import com.google.gson.Gson
import fr.isen.marcelat.e_restaurant.databinding.ActivityDetailBinding
import fr.isen.marcelat.e_restaurant.model.Item
import fr.isen.marcelat.e_restaurant.model.ItemPanier
import fr.isen.marcelat.e_restaurant.model.PanierList
import java.io.File

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var instance: Item
    private var cpt = 1
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        instance= intent.getSerializableExtra("item") as Item
        val item = intent.getSerializableExtra(MenuActivity.ITEM_KEY) as Item
        binding.detailTitle.text = item.name_fr
        binding.detailIngredient.text = item.ingredients.joinToString(", ") {it.name_fr}

        val carouselAdapter = CarouselAdapter(this, item.images)
        binding.detailSlider.adapter = carouselAdapter

        binding.nbItem.text = cpt.toString() + "  :"
        binding.totalPrice.text = (item.prices[0].price.toFloat()*cpt).toString() + "€"


        binding.Plus.setOnClickListener{
            cpt += 1
            binding.nbItem.text = cpt.toString() + "  :"
            binding.totalPrice.text = (item.prices[0].price.toFloat()*cpt).toString() + "€"
        }

        binding.Moins.setOnClickListener{
            cpt -= 1
            if(cpt<1)
            {
                cpt=1
            }
            binding.nbItem.text = cpt.toString() + "  :"
            binding.totalPrice.text = (item.prices[0].price.toFloat()*cpt).toString() + "€"
        }

        binding.AjoutPanier.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage(R.string.askUser)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ajout, DialogInterface.OnClickListener { dialog, id ->
                        addPanier()
                    })
                    .setNegativeButton(R.string.refus, DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = dialogBuilder.create()
                alert.setTitle("Panier")
                alert.show()
        }

    }

    private fun addPanier() {
        val listPanier = checkIfExist()
        val strPanier = Gson().toJson(listPanier, PanierList::class.java)
        File(cacheDir.absolutePath + "panier.json").writeText(strPanier)
        finish()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private fun checkIfExist(): PanierList? {
        var exist = false
        val file = File(cacheDir.absolutePath + "panier.json")
        val listPanier = if(file.exists()) {
            Gson().fromJson(
                file.readText(),
                PanierList::class.java
            )
        } else {
            PanierList(arrayListOf())
        }

        listPanier.info.forEach{
            if (it.item.name_fr.equals(this.instance.name_fr)) {
                exist = true
                it.quantity += this.cpt
            }
        }

        if (!exist) {
           listPanier.info.add(ItemPanier(this.instance, this.cpt))
        }
        return listPanier
    }


}