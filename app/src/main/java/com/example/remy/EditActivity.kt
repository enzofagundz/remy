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
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class EditActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var reminder: Reminder? = null

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
        reminder = reminderRepository.show(id)

        // inicializar o mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // preencher os campos do formulário com os dados do reminder
        val title = findViewById<EditText>(R.id.title)
        val description = findViewById<EditText>(R.id.description)
        val date = findViewById<EditText>(R.id.date)
        val time = findViewById<EditText>(R.id.time)
        val location = findViewById<EditText>(R.id.location)

        title.setText(reminder?.title)
        description.setText(reminder?.description)
        date.setText(reminder?.date)
        time.setText(reminder?.time)
        location.setText(reminder?.location)

        // adicionar um listener para o evento de clique no botão de salvar
        val btnSave = findViewById<Button>(R.id.save)
        btnSave.setOnClickListener {
            // atualizar o reminder com os dados do formulário
            val updatedReminder = Reminder(
                id,
                title.text.toString(),
                description.text.toString(),
                date.text.toString(),
                time.text.toString(),
                location.text.toString(),
                reminder?.latitude ?: 0.0, // mantém a latitude original se não for alterada
                reminder?.longitude ?: 0.0 // mantém a longitude original se não for alterada
            )
            reminderRepository.update(updatedReminder)
            finish()
            val intent = Intent(this, ShowActivity::class.java)
            intent.putExtra("id", updatedReminder.id)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Adicionar marcador ao mapa com a latitude e longitude do reminder
        reminder?.let {
            val reminderLocation = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(reminderLocation))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reminderLocation, 15f))
        }
    }
}
