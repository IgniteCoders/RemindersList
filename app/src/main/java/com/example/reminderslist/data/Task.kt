package com.example.reminderslist.data

class Task (
    var id: Long,
    var title: String,
    var done: Boolean = false,
    var category: Category
) {


    companion object {
        const val TABLE_NAME = "Tasks"

        const val COLUMN_NAME_ID = "id"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DONE = "done"
        const val COLUMN_NAME_CATEGORY = "category_id"
    }
}