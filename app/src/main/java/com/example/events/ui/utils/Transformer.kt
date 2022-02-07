package com.example.events.ui.utils

import android.content.Context
import android.location.Geocoder
import java.util.*

class Transformer {
    companion object {
        fun getLocation(context: Context, latitude: Double, longitude: Double,  location : (street: String, city: String) -> Unit) {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude,longitude, 1);
            val street = addresses[0].getAddressLine(0)
            val city = addresses[0].subAdminArea ?: "Sem endereço"
            location(street, city)
        }
    }
}