package com.example.island_spotter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FavoritesAdapter(
    private val context: Context,
    private val favoritesList: List<FavoritePlace>,
    private val onItemClick: (FavoritePlace) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.favorite_spots_layout, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val favoritePlace = favoritesList[position]
        holder.favoriteName.text = favoritePlace.name
        holder.favoriteDescription.text = favoritePlace.description
        val imageResId = context.resources.getIdentifier(favoritePlace.imageSrc, "drawable", context.packageName)
        holder.favoriteImage.setImageResource(imageResId)

        holder.itemView.setOnClickListener {
            onItemClick(favoritePlace) // Handle item click
        }
    }

    override fun getItemCount(): Int = favoritesList.size

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val favoriteImage: ImageView = itemView.findViewById(R.id.favorite_image)
        val favoriteName: TextView = itemView.findViewById(R.id.favorite_name)
        val favoriteDescription: TextView = itemView.findViewById(R.id.favorite_description)
    }
}
