package com.geekster.todoapp.DAO

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geekster.todoapp.models.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}