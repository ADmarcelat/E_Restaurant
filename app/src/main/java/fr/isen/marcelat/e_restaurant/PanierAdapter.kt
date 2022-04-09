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
        val imageView: ImageView = binding.itemImage
        val nameItem: TextView = binding.name
        val quantityItem: TextView = binding.quantity
        val priceItem: TextView = binding.price
        val deleteItem: ImageView = binding.delete

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PanierViewHolder {
        binding = ActivityPanierAdapterBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return PanierViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PanierViewHolder, position: Int) {
        val item = data[position]
        if (item.item.name_fr.length > 13) {
            holder.nameItem.text = item.item.name_fr.subSequence(0,13)
        }
        else {
            holder.nameItem.text = item.item.name_fr
        }
        holder.quantityItem.text = item.quantity.toString()
        var price = item.item.prices[0].price.toFloat() * item.quantity
        holder.priceItem.text = price.toString() + "€"

        val url = item.item.images[0]
        Picasso.get().load(url.ifEmpty { null }).fit().centerCrop()
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imageView);

        holder.deleteItem.setOnClickListener {
            data.remove(data[position])
            notifyItemRemoved(position)
            clickListener(item)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}