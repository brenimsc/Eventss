package com.example.events.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.events.R
import com.example.events.databinding.ItemEventsFavoritesBinding
import com.example.events.data.model.Event
import com.example.events.ui.utils.Transformer
import java.text.SimpleDateFormat
import java.util.*

class EventsFavoritesAdapter(
    val context: Context,
    val events: MutableList<Event>
) : RecyclerView.Adapter<EventsFavoritesAdapter.EventsFavoritesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsFavoritesViewHolder {
        return EventsFavoritesViewHolder(
            ItemEventsFavoritesBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsFavoritesViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    var onItemClick: ((event: Event, itemView: Boolean) -> Unit)? = null


    inner class EventsFavoritesViewHolder(private val binding: ItemEventsFavoritesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            itemView.setOnClickListener {
                onItemClick?.invoke(event, true)
            }
            binding.imgFavoriteItemFavorite.setOnClickListener {
                onItemClick?.invoke(event, false)
            }
            binding.txtTitleItemFavorite.text = event.title
            val dateFormatted = SimpleDateFormat("dd MMMM HH:mm")
            val date = Date(event.date)
            dateFormatted.format(date).capitalize()?.let {
                binding.txtDateItemFavorite.text = it
            }
            binding.imgFavoriteItemFavorite.setColorFilter(
                ContextCompat.getColor(
                    context,
                    R.color.red
                )
            )

            Glide.with(itemView).load(event.image)
                .centerCrop()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.img_sem_foto)
                .into(binding.imgItemFavorite)

            Transformer.getLocation(context, event.latitude, event.longitude) { _, city ->
                binding.txtCityItemFavorite.text = city
            }

        }

    }


}