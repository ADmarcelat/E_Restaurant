package fr.isen.marcelat.e_restaurant


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import fr.isen.marcelat.e_restaurant.model.ItemPanier
import fr.isen.marcelat.e_restaurant.databinding.ActivityPanierAdapterBinding
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso


class PanierAdapter(val data: ArrayList<ItemPanier>, val clickListener: (ItemPanier) -> Unit)
    : RecyclerView.Adapter<PanierAdapter.PanierViewHolder>(){
    private lateinit var binding: ActivityPanierAdapterBinding

    inner class PanierViewHolder (binding: ActivityPanierAdapterBinding) : RecyclerView.ViewHolder(binding.root) {
        val image: ImageView = binding.itemImage
        val name: TextView = binding.name
        val quantity: TextView = binding.quantity
        val price: TextView = binding.price
        val delete: ImageView = binding.delete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanierViewHolder {
        binding = ActivityPanierAdapterBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return PanierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PanierViewHolder, position: Int) {
        val item = data[position]
        if (item.item.name_fr.length > 13) {
            holder.name.text = item.item.name_fr.subSequence(0,13)
        }
        else {
            holder.name.text = item.item.name_fr
        }
        holder.quantity.text = item.quantity.toString()
        var price = item.item.prices[0].price.toFloat() * item.quantity
        holder.price.text = price.toString() + "€"

        val url = item.item.images[0]
        Picasso.get().load(url.ifEmpty { null }).fit().centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(holder.image);

        holder.delete.setOnClickListener {
            data.remove(data[position])
            notifyItemRemoved(position)
            clickListener(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}