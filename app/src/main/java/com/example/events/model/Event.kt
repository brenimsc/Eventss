package com.example.events.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Event(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Float,
    val latitude: Double,
    val longitude: Double,
    val image: String,
    val description: String,
    val date: Long,
    var favorites: Boolean = false,
    var checkin: Boolean = false
)
