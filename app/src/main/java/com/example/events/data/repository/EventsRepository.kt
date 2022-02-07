package com.example.events.data.repository

import androidx.lifecycle.LiveData
import com.example.events.data.database.EventsDao
import com.example.events.data.service.EventsApiService
import com.example.events.data.model.Event
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow

class EventsRepository(val service: EventsApiService, val dao: EventsDao) {

    fun getListCheckins(): LiveData<List<Event>?> = dao.getCheckinEvents()

    fun getFavorites() = dao.getFavoritesEvents()

    suspend fun alterToFavorites(event: Event) {
        dao.updateEvent(event)
    }

    suspend fun getListEvents() = flow {
        try {
            val call = service.listEvents()

            if (call.isSuccessful) {
                call.body()?.let { events: List<Event> ->
                    //logica para funcionar favoritos e checkin
                    val listInternal: List<Event>? = dao.getEvents()
                    checkIfItFavoritesOrCheckin(events, listInternal)
                    saveAndEmitEvents(events)
                }
            } else {
                emit(Exception("Deu erro"))
            }

        } catch (e: Exception) {
            emit(Exception("Erro $e"))
        }
    }

    private suspend fun FlowCollector<List<Event>>.saveAndEmitEvents(events: List<Event>) {
        events?.let {
            dao.saveEvents(it)
            emit(it)
        }
    }

    private fun checkIfItFavoritesOrCheckin(
        events: List<Event>,
        listInternal: List<Event>?
    ) {
        events?.map { event ->
            if (listInternal != null) {
                for (item in listInternal) {
                    if (event.id == item.id) {
                        event.favorites = item.favorites
                        event.checkin = item.checkin
                        return@map
                    }
                }
            }
        }
    }

}