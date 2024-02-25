package com.geekster.todoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.geekster.todoapp.ViewModel.TaskViewModel
import com.geekster.todoapp.databinding.ListItemTaskBinding
import com.geekster.todoapp.models.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskListAdapter(private val viewModel: TaskViewModel) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ListItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
    }

    inner class TaskViewHolder(private val binding: ListItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.apply {
                taskNameTextView.text = task.taskName
                dueDateTextView.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(
                    Date(task.dueDate)
                )
                completeCheckBox.isChecked = task.isCompleted

                completeCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    task.isCompleted = isChecked
                    viewModel.update(task)
                }
                taskNameTextView.setOnClickListener {
                    taskNameTextView.visibility = View.INVISIBLE

                    // Show the EditText
                    taskNameEditText.visibility = View.VISIBLE
                    updateBtn.visibility = View.VISIBLE
                    taskNameEditText.setText(task.taskName) // Set text from task
                    taskNameEditText.requestFocus() // Set focus to EditText
                }
                updateBtn.setOnClickListener {
                    task.taskName = taskNameEditText.text.toString()
                    viewModel.update(task)
                    taskNameTextView.text = task.taskName
                    taskNameTextView.visibility = View.VISIBLE
                    taskNameEditText.visibility = View.INVISIBLE
                    updateBtn.visibility = View.INVISIBLE
                }
            }
        }
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}