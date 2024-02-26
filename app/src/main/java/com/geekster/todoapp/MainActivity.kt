package com.geekster.todoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.geekster.todoapp.ViewModel.TaskViewModel
import com.geekster.todoapp.databinding.ActivityMainBinding
import com.geekster.todoapp.models.Task
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TaskViewModel by viewModels()
    private lateinit var taskListAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        taskListAdapter = TaskListAdapter(viewModel)
        binding.taskRecyclerView.adapter = taskListAdapter

        binding.deleteCompletedTasksButton.setOnClickListener {
            viewModel.deleteCompletedTasks()
        }

        // Initialize MaterialDatePicker
        val builder = MaterialDatePicker.Builder.datePicker()
        val datePicker = builder.build()

        // Set up due date EditText click listener to show calendar
        binding.dueDateEditText.setOnClickListener {
            datePicker.show(supportFragmentManager, "Date Picker")
        }

        var formattedDate = ""
        // Set up due date selection listener

        datePicker.addOnPositiveButtonClickListener { timestamp ->
            val calendar = Calendar.getInstance().time
            val selectedDate = Date(timestamp)
            if (selectedDate < calendar){
                Toast.makeText(applicationContext, "Deadline can't be set on or before current Date", Toast.LENGTH_SHORT).show()
                binding.addTaskButton.isClickable = false
            }
            else if(selectedDate >= calendar){
                formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
                binding.dueDateEditText.setText(formattedDate)
                binding.addTaskButton.isClickable = true
            }

        }




        binding.addTaskButton.setOnClickListener {
            val taskName = binding.taskNameEditText.text.toString()
            val dueDateText = binding.dueDateEditText.text.toString()
            val isCompleted = binding.completedCheckBox.isChecked

            if (taskName.isNotBlank() && dueDateText.isNotBlank()) {
                // Parse the due date string into a Long value representing milliseconds
                val dueDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dueDateText)?.time ?: 0L

                val task = Task(taskName = taskName, dueDate = dueDate, isCompleted = isCompleted)


                viewModel.insert(task)

                // Clear input fields after adding the task
                binding.taskNameEditText.text.clear()
                binding.dueDateEditText.text?.clear()
                binding.completedCheckBox.isChecked = false
            } else {
                Toast.makeText(this, "Task name and due date are required", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.allTasks.observe(this) { tasks ->
            tasks?.let {
                taskListAdapter.submitList(it)
            }
        }
    }
}