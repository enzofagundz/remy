package com.example.remy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.remy.database.entities.Reminder
import com.example.remy.repository.ReminderRepository

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getIntExtra("id", 0)
        if (id == 0) finish()

        // recuperar o reminder pelo id
        val reminderRepository = ReminderRepository(this)
        val reminder = reminderRepository.show(id)

        // preencher os campos do formulário com os dados do reminder

        val title = findViewById<EditText>(R.id.title)
        val description = findViewById<EditText>(R.id.description)
        val date = findViewById<EditText>(R.id.date)
        val time = findViewById<EditText>(R.id.time)
        val location = findViewById<EditText>(R.id.location)
        val latitude = findViewById<EditText>(R.id.latitude)
        val longitude = findViewById<EditText>(R.id.longitude)

        title.setText(reminder?.title)
        description.setText(reminder?.description)
        date.setText(reminder?.date)
        time.setText(reminder?.time)
        location.setText(reminder?.location)
        latitude.setText(reminder?.latitude.toString())
        longitude.setText(reminder?.longitude.toString())

        // adicionar um listener para o evento de clique no botão de excluir
        val btnSave = findViewById<Button>(R.id.save)
        btnSave.setOnClickListener {
            // atualizar o reminder com os dados do formulário
            val reminder = Reminder(
                id,
                title.text.toString(),
                description.text.toString(),
                date.text.toString(),
                time.text.toString(),
                location.text.toString(),
                latitude.text.toString().toDouble(),
                longitude.text.toString().toDouble()
            )
            reminderRepository.update(reminder)
            finish()
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("id", reminder.id)
            startActivity(intent)
        }
    }
}