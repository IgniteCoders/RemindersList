package com.example.reminderslist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.reminderslist.data.Task
import com.example.reminderslist.databinding.ItemTaskBinding
import com.example.reminderslist.utils.TaskDiffUtil
import com.example.reminderslist.utils.addStrikethrough

class TaskAdapter(
    var items: List<Task>,
    val onClick: (Int) -> Unit,
    val onDelete: (Int) -> Unit,
    val onCheck: (Int) -> Unit
) : Adapter<TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = items[position]
        holder.render(task)
        holder.itemView.setOnClickListener {
            onClick(holder.adapterPosition)
        }
        holder.binding.deleteButton.setOnClickListener {
            onDelete(holder.adapterPosition)
        }
        holder.binding.doneCheckBox.setOnCheckedChangeListener { _, _ ->
            if (holder.binding.doneCheckBox.isPressed) {
                onCheck(holder.adapterPosition)
            }
        }
    }

    fun updateItems(items: List<Task>) {
        val diffUtils = TaskDiffUtil(this.items, items)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        this.items = items
        diffResult.dispatchUpdatesTo(this)
    }
}

class TaskViewHolder(val binding: ItemTaskBinding) : ViewHolder(binding.root) {

    fun render(task: Task) {
        if (task.done) {
            binding.titleTextView.text = task.title.addStrikethrough()
        } else {
            binding.titleTextView.text = task.title
        }
        binding.doneCheckBox.isChecked = task.done
    }
}