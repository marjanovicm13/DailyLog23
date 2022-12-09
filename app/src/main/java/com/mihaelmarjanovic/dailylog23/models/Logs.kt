package com.mihaelmarjanovic.dailylog23.models

import android.media.Image
import android.net.Uri
import androidx.annotation.Nullable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "logs")
data class Logs(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "log")
    val log: String,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "time")
    val timeOfLog: String,
    @Nullable
    @ColumnInfo(name = "image")
    val image: String
): Serializable