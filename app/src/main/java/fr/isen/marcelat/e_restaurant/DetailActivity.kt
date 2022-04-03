package fr.isen.marcelat.e_restaurant

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import fr.isen.marcelat.e_restaurant.databinding.ActivityDetailBinding
import fr.isen.marcelat.e_restaurant.model.Item

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private var cpt = 0;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item = intent.getSerializableExtra(MenuActivity.ITEM_KEY) as Item
        binding.detailTitle.text = item.name_fr
        binding.detailIngredient.text = item.ingredients.joinToString(", ") {it.name_fr}

        val carouselAdapter = CarouselAdapter(this, item.images)
        binding.detailSlider.adapter = carouselAdapter



        binding.Plus.setOnClickListener{
            cpt += 1;
            binding.nbItem.text = cpt.toString() + "  :";
            binding.totalPrice.text = (item.prices[0].price.toFloat()*cpt).toString() + "€"
        }

        binding.Moins.setOnClickListener{
            cpt -= 1;
            if(cpt<1)
            {
                cpt=0
            }
            binding.nbItem.text = cpt.toString()+ "  :";
            binding.totalPrice.text = (item.prices[0].price.toFloat()*cpt).toString() + "€"
        }

        binding.AjoutPanier.setOnClickListener {

        }







    }

}