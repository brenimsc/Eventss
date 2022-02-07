package com.example.events.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.events.R
import com.example.events.databinding.ItemEventsBinding
import com.example.events.data.model.Event
import com.example.events.ui.utils.Transformer
import java.text.SimpleDateFormat
import java.util.*

class EventsAdapter(
    val context: Context,
    val events: MutableList<Event>
) :
    RecyclerView.Adapter<EventsAdapter.EventsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
        return EventsViewHolder(
            ItemEventsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    var onItemClick: ((Int) -> Unit)? = null


    inner class EventsViewHolder(val binding: ItemEventsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            itemView.setOnClickListener {
                onItemClick?.invoke(event.id)
            }
            binding.txtTitleEvent.text = event.title
            binding.txtDescriptionEvent.text = event.description
            Glide.with(itemView).load(event.image)
                .centerCrop()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.img_sem_foto)
                .into(binding.imgBanner)


            val dateFormatted = SimpleDateFormat("dd MMMM HH:mm")
            val date = Date(event.date)
            dateFormatted.format(date).capitalize()?.let {
                val date = it.split(" ")
                binding.txtDay.text = date[0].substring(0, 2)
                binding.txtMonth.text = date[1].substring(0, 3)
                binding.txtHour.text = date[2]
            }

            Transformer.getLocation(context, event.latitude, event.longitude) { _, city ->
                binding.txtCity.text = city
            }
        }

    }


}