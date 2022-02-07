package com.example.events.di

import com.example.events.model.Checkin
import com.example.events.model.Event
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EventsApiService {

    @GET("events")
    suspend fun listEvents() : Response<List<Event>>

    @GET("events/{id}")
    suspend fun getEvent(@Path("id") id: Int): Event

    @POST("checkin")
    suspend fun doCheckin(@Body checkin: Checkin)

}
