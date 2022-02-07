package com.example.events.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.events.R
import com.example.events.databinding.ItemEventsCheckinBinding
import com.example.events.data.model.Event

class EventsChekinAdapter(
    val context: Context,
    val events: MutableList<Event>
) : RecyclerView.Adapter<EventsChekinAdapter.EventsChekinViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsChekinViewHolder {
        return EventsChekinViewHolder(
            ItemEventsCheckinBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventsChekinViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size

    var onItemClick: ((Int) -> Unit)? = null


    inner class EventsChekinViewHolder(val binding: ItemEventsCheckinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: Event) {
            itemView.setOnClickListener {
                onItemClick?.invoke(event.id)
            }
            binding.txtTitleCheckin.text = event.title
            binding.txtPriceCheckin.text = context.getString(R.string.priceCheckin, event.price)
            Glide.with(itemView).load(event.image)
                .centerCrop()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.img_sem_foto)
                .into(binding.imgItemCheckin)

        }
    }


}