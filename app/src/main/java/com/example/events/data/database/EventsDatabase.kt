package com.example.events.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.events.ui.constants.Constants.DATABASE_NAME
import com.example.events.data.model.Event

@Database(entities = [Event::class], version = 1, exportSchema = false)
abstract class EventsDatabase : RoomDatabase() {

    abstract val eventsDao: EventsDao

    companion object {
        @Volatile
        private var INSTANCE: EventsDatabase? = null

        fun getInstance(context: Context): EventsDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventsDatabase::class.java,
                        DATABASE_NAME
                    )
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}