package com.mihaelmarjanovic.dailylog23.database

import android.app.Application
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mihaelmarjanovic.dailylog23.fragments.DayFragment
import com.mihaelmarjanovic.dailylog23.models.Day
import com.mihaelmarjanovic.dailylog23.models.Goals
import com.mihaelmarjanovic.dailylog23.models.Logs
import com.mihaelmarjanovic.dailylog23.utilities.DATABASE_NAME

@Database(entities = arrayOf(Logs::class, Goals::class, Day::class), version = 34,exportSchema = false)
abstract class LogsDatabase: RoomDatabase() {

    abstract fun getLogsDao(): LogsDao
    abstract fun getGoalsDao(): GoalsDao
    abstract fun getDayDao(): DayDao

    companion object{

        @Volatile
        private var INSTANCE: LogsDatabase? = null

        fun getDatabase(context: Application): LogsDatabase{

            return INSTANCE ?: synchronized(this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LogsDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance

            }
        }
    }
}