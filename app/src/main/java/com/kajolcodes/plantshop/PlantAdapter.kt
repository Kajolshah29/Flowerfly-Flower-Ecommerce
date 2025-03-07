package com.kajolcodes.plantshop

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PlantAdapter(private val context: Context, private var plantList: ArrayList<Plant>) :
    RecyclerView.Adapter<PlantAdapter.PlantViewHolder>() {

    class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val plantImage: ImageView = itemView.findViewById(R.id.plantImage)
        val plantName: TextView = itemView.findViewById(R.id.plantName)
        val plantDesc: TextView = itemView.findViewById(R.id.plantDesc)
        val plantPrice: TextView = itemView.findViewById(R.id.plantPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_plant, parent, false)
        return PlantViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val plant = plantList[position]
        holder.plantName.text = plant.name
        holder.plantDesc.text = plant.desc
        holder.plantPrice.text = "Price: ${plant.price}"

        Glide.with(context)
            .load(plant.ImageUrl)
            .into(holder.plantImage)
    }

    override fun getItemCount(): Int = plantList.size
}
