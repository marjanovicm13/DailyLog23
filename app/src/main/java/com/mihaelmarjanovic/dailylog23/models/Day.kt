package com.mihaelmarjanovic.dailylog23.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "day")
data class Day(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "rating")
    val rating: Float?,
    @ColumnInfo(name = "date")
    val date: String
): Serializable
