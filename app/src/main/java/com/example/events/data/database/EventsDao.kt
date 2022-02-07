package com.example.events.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.events.data.model.Event

@Dao
interface EventsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvents(events: List<Event>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveEvent(event: Event)

    @Query("SELECT * FROM Event")
    fun getAllEvents(): LiveData<List<Event>?>

    @Query("SELECT * FROM Event")
    suspend fun getEvents(): List<Event>

    @Query("SELECT * FROM Event WHERE id =:id")
    suspend fun getEvent(id: Int): Event

    @Update
    suspend fun updateEvent(event: Event)

    @Query("SELECT * FROM Event WHERE favorites = 1")
    fun getFavoritesEvents(): LiveData<List<Event>?>

    @Query("SELECT * FROM Event WHERE checkin = 1")
    fun getCheckinEvents(): LiveData<List<Event>?>
}