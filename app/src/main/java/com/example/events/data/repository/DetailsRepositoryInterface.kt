package com.example.events.data.repository

import com.example.events.data.model.Checkin
import com.example.events.data.model.Event
import com.example.events.data.model.User

interface DetailsRepositoryInterface {

    suspend fun doCheckin(checkin: Checkin)

    suspend fun alterCheckin(event: Event)

    fun getUser(): String?

    suspend fun alterToFavorites(event: Event)

    fun saveUser(user: User)

    suspend fun getEventId(id: Int): Event
}