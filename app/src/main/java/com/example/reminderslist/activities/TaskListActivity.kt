package com.example.reminderslist.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.reminderslist.R
import com.example.reminderslist.adapters.TaskAdapter
import com.example.reminderslist.data.Category
import com.example.reminderslist.data.CategoryDAO
import com.example.reminderslist.data.Task
import com.example.reminderslist.data.TaskDAO
import com.example.reminderslist.databinding.ActivityMainBinding
import com.example.reminderslist.databinding.ActivityTaskListBinding

class TaskListActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_ID = "CATEGORY_ID"
    }

    lateinit var binding: ActivityTaskListBinding

    lateinit var taskDAO: TaskDAO
    lateinit var categoryDAO: CategoryDAO
    lateinit var taskList: List<Task>
    lateinit var category: Category

    lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityTaskListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getLongExtra(CATEGORY_ID, -1L)

        taskDAO = TaskDAO(this)
        categoryDAO = CategoryDAO(this)

        category = categoryDAO.findById(id)!!

        supportActionBar?.title = category.title

        adapter = TaskAdapter(emptyList(), ::editTask, ::deleteTask, ::checkTask)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        binding.addTaskButton.setOnClickListener {
            val intent = Intent(this, TaskActivity::class.java)
            intent.putExtra(TaskActivity.CATEGORY_ID, category.id)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        refreshData()
    }

    fun refreshData() {
        taskList = taskDAO.findAllByCategory(category)
        adapter.updateItems(taskList)
    }

    fun checkTask(position: Int) {
        val task = taskList[position]

        task.done = !task.done
        taskDAO.update(task)
        refreshData()
    }

    fun editTask(position: Int) {
        val task = taskList[position]

        val intent = Intent(this, TaskActivity::class.java)
        intent.putExtra(TaskActivity.TASK_ID, task.id)
        intent.putExtra(TaskActivity.CATEGORY_ID, category.id)
        startActivity(intent)
    }

    fun deleteTask(position: Int) {
        val task = taskList[position]

        AlertDialog.Builder(this)
            .setTitle("Delete task")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton(android.R.string.ok) { _, _ ->
                taskDAO.delete(task)
                refreshData()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .setCancelable(false)
            .show()
    }
}