package com.example.remy

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.remy.database.entities.Reminder
import com.example.remy.repository.ReminderRepository

class IndexActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_index)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val reminderRepository = ReminderRepository(this)
        val reminders = reminderRepository.index()
        if (reminders.isEmpty()) {
            val reminder = Reminder(
                title = "Comprar pão",
                description = "Pão francês",
                date = "2021-09-01",
                time = "08:00",
                location = "Padaria",
                latitude = -23.5505199,
                longitude = -46.6333094
            )

            reminderRepository.create(reminder)
        }

        // inserir os reminders.toArray() no ListView
        val listView = findViewById<ListView>(R.id.listView)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, reminders)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val reminder = reminders[position]
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("id", reminder.id)
            startActivity(intent)
        }
    }
}