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
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class CreateActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var selectedLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_create)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // inicializar o mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val btnCreate = findViewById<Button>(R.id.create)
        btnCreate.setOnClickListener {
            // criar um reminder
            val title = findViewById<EditText>(R.id.title).text.toString()
            val description = findViewById<EditText>(R.id.description).text.toString()
            val date = findViewById<EditText>(R.id.date).text.toString()
            val time = findViewById<EditText>(R.id.time).text.toString()
            val location = findViewById<EditText>(R.id.location).text.toString()
//            val latitude = findViewById<EditText>(R.id.latitude).text.toString().toDouble()
//            val longitude = findViewById<EditText>(R.id.longitude).text.toString().toDouble()
            val latitude = selectedLatLng?.latitude ?: 0.0
            val longitude = selectedLatLng?.longitude ?: 0.0

            // salvar o reminder no banco de dados
            val reminder = Reminder(
                title = title,
                description = description,
                date = date,
                time = time,
                location = location,
                latitude = latitude,
                longitude = longitude
            )

            val reminderRepository = ReminderRepository(this)
            reminderRepository.create(reminder)

            // redirecionar para a tela de listagem
            finish()
            val intent = Intent(this, IndexActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        mMap?.setOnMapClickListener { latLng ->
            selectedLatLng = latLng
            mMap?.clear()
            mMap?.addMarker(MarkerOptions().position(latLng))
        }
    }
}