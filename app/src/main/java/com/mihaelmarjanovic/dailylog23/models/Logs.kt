package com.mihaelmarjanovic.dailylog23.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "logs")
class Logs: Serializable {
    @PrimaryKey(autoGenerate = true)
    val id = 0

    @ColumnInfo(name = "title")
    val title = ""

    @ColumnInfo(name = "log")
    val log = ""

    @ColumnInfo(name = "date")
    val date = ""

}