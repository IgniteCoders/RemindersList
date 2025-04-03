package com.example.reminderslist.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.example.reminderslist.utils.DatabaseManager

class CategoryDAO(context: Context) {

    val databaseManager = DatabaseManager(context)

    fun insert(category: Category) {
        // Gets the data repository in write mode
        val db = databaseManager.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(Category.COLUMN_NAME_TITLE, category.title)
        }

        try {
            // Insert the new row, returning the primary key value of the new row
            val newRowId = db.insert(Category.TABLE_NAME, null, values)

            Log.i("DATABASE", "Inserted category with id: $newRowId")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun update(category: Category) {
        // Gets the data repository in write mode
        val db = databaseManager.writableDatabase

        // Create a new map of values, where column names are the keys
        val values = ContentValues().apply {
            put(Category.COLUMN_NAME_TITLE, category.title)
        }

        try {
            val updatedRows = db.update(Category.TABLE_NAME, values, "${Category.COLUMN_NAME_ID} = ${category.id}", null)

            Log.i("DATABASE", "Updated category with id: ${category.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun delete(category: Category) {
        val db = databaseManager.writableDatabase

        try {
            val deletedRows = db.delete(Category.TABLE_NAME, "${Category.COLUMN_NAME_ID} = ${category.id}", null)

            Log.i("DATABASE", "Deleted category with id: ${category.id}")
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }
    }

    fun findById(id: Long): Category? {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(
            Category.COLUMN_NAME_ID,
            Category.COLUMN_NAME_TITLE
        )

        val selection = "${Category.COLUMN_NAME_ID} = $id"

        var category: Category? = null

        try {
            val cursor = db.query(
                Category.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            if (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))

                category = Category(id, title)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return category
    }

    fun findAll(): List<Category> {
        val db = databaseManager.readableDatabase

        val projection = arrayOf(
            Category.COLUMN_NAME_ID,
            Category.COLUMN_NAME_TITLE
        )

        var categoryList: MutableList<Category> = mutableListOf()

        try {
            val cursor = db.query(
                Category.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
            )

            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_ID))
                val title = cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE))

                val category = Category(id, title)
                categoryList.add(category)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.close()
        }

        return categoryList
    }
}