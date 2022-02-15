package com.example.events.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.events.data.database.EventsDao
import com.example.events.data.model.Checkin
import com.example.events.data.model.Event
import com.example.events.data.model.User
import com.example.events.data.service.EventsApiService
import com.example.events.ui.constants.Constants.KEY_USER
import com.google.gson.Gson

class DetailsRepository(
    val service: EventsApiService,
    val dao: EventsDao,
    val preferences: SharedPreferences
) : DetailsRepositoryInterface {

    override suspend fun doCheckin(checkin: Checkin) = service.doCheckin(checkin)

    override suspend fun alterCheckin(event: Event) = dao.updateEvent(event)

    override fun getUser(): String? = preferences.getString(KEY_USER, "")

    override suspend fun alterToFavorites(event: Event) {
        dao.updateEvent(event)
    }

    override fun saveUser(user: User) {
        preferences.edit {
            putString(KEY_USER, Gson().toJson(user))
        }
    }

    override suspend fun getEventId(id: Int): Event {
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
