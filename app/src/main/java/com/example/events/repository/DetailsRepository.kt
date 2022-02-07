package com.example.events.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.events.constants.Constants.KEY_USER
import com.example.events.database.EventsDao
import com.example.events.di.EventsApiService
import com.example.events.model.Checkin
import com.example.events.model.Event
import com.example.events.model.User
import com.google.gson.Gson

class DetailsRepository(
    val service: EventsApiService,
    val dao: EventsDao,
    val preferences: SharedPreferences
) {

    suspend fun doCheckin(checkin: Checkin) = service.doCheckin(checkin)

    suspend fun alterCheckin(event: Event) = dao.updateEvent(event)

    fun getUser() = preferences.getString(KEY_USER, "")

    suspend fun alterToFavorites(event: Event) {
        dao.updateEvent(event)
    }

    fun saveUser(user: User) {
        preferences.edit {
            putString(KEY_USER, Gson().toJson(user))
        }
    }

    suspend fun getEventId(id: Int): Event {
        //Logica para funcionar funcionalidade implementada apenas do app (Favoritos e controle de Checkin)
        val event = service.getEvent(id)
        val eventInternal = dao.getEvent(id)
        eventInternal?.let {
            event.favorites = eventInternal.favorites
            event.checkin = eventInternal.checkin
        }
        return event
    }


}
