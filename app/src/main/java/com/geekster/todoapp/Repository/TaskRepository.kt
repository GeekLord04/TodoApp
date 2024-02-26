package com.geekster.todoapp.Repository

import androidx.lifecycle.LiveData
import com.geekster.todoapp.DAO.TaskDao
import com.geekster.todoapp.models.Task
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDao: TaskDao) {
    val allTasks: LiveData<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun deleteCompletedTasks() {
        taskDao.deleteCompletedTasks()
    }

}