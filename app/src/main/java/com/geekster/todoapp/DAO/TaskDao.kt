package com.geekster.todoapp.DAO

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.geekster.todoapp.models.Task

@Dao
interface TaskDao {
    @Query("SELECT * FROM my_tasks ORDER BY dueDate DESC")
    fun getAllTasks(): LiveData<List<Task>>

    @Insert
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM my_tasks WHERE isCompleted = 1")
    suspend fun deleteCompletedTasks()



}