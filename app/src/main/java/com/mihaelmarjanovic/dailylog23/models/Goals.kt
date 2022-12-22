package com.mihaelmarjanovic.dailylog23.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "goals")
data class Goals (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "isChecked")
    val isChecked: Boolean,
    @ColumnInfo(name = "goal")
    val goal: String,
    @ColumnInfo(name = "date")
    val date: String
): Serializable