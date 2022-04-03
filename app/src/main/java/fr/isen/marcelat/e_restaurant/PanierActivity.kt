package fr.isen.marcelat.e_restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import fr.isen.marcelat.e_restaurant.databinding.ActivityPanierBinding

class PanierActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPanierBinding
    private lateinit var monRecycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPanierBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}