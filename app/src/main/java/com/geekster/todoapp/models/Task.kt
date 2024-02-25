package com.geekster.todoapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var taskName: String,
    var dueDate: Long,
    var isCompleted: Boolean
)