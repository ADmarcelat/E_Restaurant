package fr.isen.marcelat.e_restaurant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.isen.marcelat.e_restaurant.model.Item

internal class CustomAdapter(private val itemsList: List<Item>, val clickListener: (Item) -> Unit) :
    RecyclerView.Adapter<CustomAdapter.CategoryViewHolder>() {

    internal inner class CategoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var price: TextView = view.findViewById(R.id.itemPrice)
        var name: TextView = view.findViewById(R.id.itemName)
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return CategoryViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = itemsList[position]
        val url = item.images[0]
        holder.name.text = item.name_fr
        holder.price.text = item.prices[0].price +" €"

        Picasso.get().load(url.ifEmpty { null }).fit()
            .placeholder(R.drawable.ic_launcher_foreground)
            .into(holder.imageView);

        holder.itemView.setOnClickListener{
            clickListener(item)
        }
    }
    override fun getItemCount(): Int {
        return itemsList.size
    }
}