package com.example.events.data.repository

import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.events.data.database.EventsDao
import com.example.events.data.model.Event
import com.example.events.data.service.EventsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import java.net.UnknownHostException

class EventsRepository(val service: EventsApiService, val dao: EventsDao) :
    EventsRepositoryInterface {

    override fun getListCheckins(): LiveData<List<Event>?> = dao.getCheckinEvents()

    override fun getFavorites(): LiveData<List<Event>?> = dao.getFavoritesEvents()

    override suspend fun alterToFavorites(event: Event) {
        dao.updateEvent(event)
    }

    override suspend fun getListEvents(): Flow<List<Event>> = flow {
        try {
            val listEvents = service.listEvents()
            listEvents?.let { events: List<Event> ->
                //logica para funcionar favoritos e checkin
                val listInternal: List<Event>? = dao.getEvents()
                checkIfItFavoritesOrCheckin(events, listInternal)
                saveAndEmitEvents(events)
            }

        } catch (e: UnknownHostException) {
            Log.e("BRENOL", e.message.toString())
            throw RemoteException("Sem conexão")
        } catch (e: Exception) {
            throw RemoteException("Não foi possivel conectar com o servidor")
        }
    }

    private suspend fun FlowCollector<List<Event>>.saveAndEmitEvents(events: List<Event>) {
        events.let {
            dao.saveEvents(it)
            emit(it)
        }
    }

    private fun checkIfItFavoritesOrCheckin(
        events: List<Event>,
        listInternal: List<Event>?
    ) {
        events.map { event ->
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