package com.example.remy.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.remy.database.DBHelper
import com.example.remy.database.entities.Reminder

// essa classe representa o repositório de lembretes
// ela define as operações que podem ser realizadas com os lembretes

class ReminderRepository(context: Context) {
    private val dbHelper = DBHelper(context)

    fun index(): List<Reminder> {
        val db = dbHelper.readableDatabase
        val cursor = db.query("reminders", null, null, null, null, null, null)
        val reminders = mutableListOf<Reminder>()

        with(cursor) {
            while (moveToNext()) {
                val idIndex = getColumnIndex("id")
                val titleIndex = getColumnIndex("title")
                val descriptionIndex = getColumnIndex("description")
                val dateIndex = getColumnIndex("date")
                val timeIndex = getColumnIndex("time")
                val locationIndex = getColumnIndex("location")
                val latitudeIndex = getColumnIndex("latitude")
                val longitudeIndex = getColumnIndex("longitude")

                if (idIndex != -1 && titleIndex != -1 && descriptionIndex != -1 && dateIndex != -1 && timeIndex != -1 && locationIndex != -1 && latitudeIndex != -1 && longitudeIndex != -1) {
                    val reminder = Reminder(
                        getInt(idIndex),
                        getString(titleIndex),
                        getString(descriptionIndex),
                        getString(dateIndex),
                        getString(timeIndex),
                        getString(locationIndex),
                        getDouble(latitudeIndex),
                        getDouble(longitudeIndex)
                    )
                    reminders.add(reminder)
                }
            }
        }

        cursor.close()
        db.close()

        return reminders
    }

    fun show(id: Int): Reminder? {
        val db = dbHelper.readableDatabase
        val cursor = db.query("reminders", null, "id = ?", arrayOf(id.toString()), null, null, null)
        var reminder: Reminder? = null

        with(cursor) {
            if (moveToNext()) {
                val titleIndex = getColumnIndex("title")
                val descriptionIndex = getColumnIndex("description")
                val dateIndex = getColumnIndex("date")
                val timeIndex = getColumnIndex("time")
                val locationIndex = getColumnIndex("location")
                val latitudeIndex = getColumnIndex("latitude")
                val longitudeIndex = getColumnIndex("longitude")

                if (titleIndex != -1 && descriptionIndex != -1 && dateIndex != -1 && timeIndex != -1 && locationIndex != -1 && latitudeIndex != -1 && longitudeIndex != -1) {
                    reminder = Reminder(
                        id,
                        getString(titleIndex),
                        getString(descriptionIndex),
                        getString(dateIndex),
                        getString(timeIndex),
                        getString(locationIndex),
                        getDouble(latitudeIndex),
                        getDouble(longitudeIndex)
                    )
                }
            }
        }

        cursor.close()
        db.close()
        return reminder
    }

    fun create(reminder: Reminder) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", reminder.title)
            put("description", reminder.description)
            put("date", reminder.date)
            put("time", reminder.time)
            put("location", reminder.location)
            put("latitude", reminder.latitude)
            put("longitude", reminder.longitude)
        }

        db.insert("reminders", null, values)
        db.close()
    }

    fun update(reminder: Reminder) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("title", reminder.title)
            put("description", reminder.description)
            put("date", reminder.date)
            put("time", reminder.time)
            put("location", reminder.location)
            put("latitude", reminder.latitude)
            put("longitude", reminder.longitude)
        }

        db.update("reminders", values, "id = ?", arrayOf(reminder.id.toString()))
        db.close()
    }


    fun delete(id: Int) {
        val db = dbHelper.writableDatabase
        db.delete("reminders", "id = ?", arrayOf(id.toString()))
        db.close()
    }
}