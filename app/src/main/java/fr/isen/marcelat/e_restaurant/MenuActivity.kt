package fr.isen.marcelat.e_restaurant



import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson

import fr.isen.marcelat.e_restaurant.databinding.ActivityMenuBinding
import fr.isen.marcelat.e_restaurant.model.DataResult
import fr.isen.marcelat.e_restaurant.model.Item
import org.json.JSONObject


class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding
    private val itemsList =  ArrayList<Item>()
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        //Define instance of binding
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Get the category user clicked on and change the title
        val getCategory: String = intent.getStringExtra("category").toString()
        title = getCategory

        //Setup du recycler view

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = CustomAdapter(itemsList) {} //On passe une fonction lambda dans les parametres
        loadDataFromServerByCategory(getCategory)
    }

    private fun  loadDataFromServerByCategory(category: String){
        val queue = Volley.newRequestQueue(this)
        val url = "http://test.api.catering.bluecodegames.com/menu"

        val jsonObject = JSONObject()
        jsonObject.put("id_shop", 1)

        val stringReq = JsonObjectRequest(Request.Method.POST, url, jsonObject,
            {  response ->
                val strResp = response.toString()
                Log.d("API", strResp)
                val dataResult = Gson().fromJson(strResp, DataResult::class.java)
               fillList(dataResult)

                binding.recyclerView.adapter = CustomAdapter(itemsList) {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(ITEM_KEY, it)
                    startActivity(intent)
                } //On passe une fonction lambda dans les parametres
            },{
                Log.d("API", "message ${it.message}")
            })
        queue.add(stringReq)
    }

    companion object{
        const val ITEM_KEY = "item"
    }

    private fun fillList(apiData: DataResult){
        if(intent.getStringExtra("category") == "Entrees"){
            apiData.data[0].items.forEach{ item: Item -> itemsList.add(item)}
        }
        if(intent.getStringExtra("category") == "Plats"){
            apiData.data[1].items.forEach { item: Item -> itemsList.add(item) }
        }
        if(intent.getStringExtra("category") == "Desserts"){
            apiData.data[2].items.forEach{ item: Item -> itemsList.add(item)}
        }
    }
}