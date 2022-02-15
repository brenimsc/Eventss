package com.example.events.data.repository

import androidx.lifecycle.LiveData
import com.example.events.data.model.Event
import kotlinx.coroutines.flow.Flow

interface EventsRepositoryInterface {

    fun getListCheckins(): LiveData<List<Event>?>

    fun getFavorites(): LiveData<List<Event>?>

    suspend fun alterToFavorites(event: Event)

    suspend fun getListEvents(): Flow<List<Event>>
}