package com.example.events.data.service

import com.example.events.data.model.Checkin
import com.example.events.data.model.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsApiService {

    @GET("/api/events")
    suspend fun listEvents() : List<Event>?

    @GET("/api/events/{id}")
    suspend fun getEvent(@Path("id") id: Int): Event

    @POST("/api/checkin")
    suspend fun doCheckin(@Body checkin: Checkin)

}
