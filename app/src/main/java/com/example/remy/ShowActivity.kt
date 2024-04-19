package com.example.remy

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
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

class ShowActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var reminder: Reminder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // recuperar o reminder pelo id
        val id = intent.getIntExtra("id", 0)
        if (id == 0) finish()

        // exibir os dados do reminder na tela
        val reminderRepository = ReminderRepository(this)
        reminder = reminderRepository.show(id)

        // inicializar o mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val title = findViewById<TextView>(R.id.title)
        val description = findViewById<TextView>(R.id.description)
        val date = findViewById<TextView>(R.id.date)
        val time = findViewById<TextView>(R.id.time)
        val location = findViewById<TextView>(R.id.location)
        val latitude = findViewById<TextView>(R.id.latitude)
        val longitude = findViewById<TextView>(R.id.longitude)

        title.append(reminder?.title)
        description.append(reminder?.description)
        date.append(reminder?.date)
        time.append(reminder?.time)
        location.append(reminder?.location)
        latitude.append(reminder?.latitude.toString())
        longitude.append(reminder?.longitude.toString())

        val btnEdit = findViewById<TextView>(R.id.edit)
        val btnDelete = findViewById<TextView>(R.id.delete)

        btnEdit.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            intent.putExtra("id", reminder?.id)
            startActivity(intent)
        }

        btnDelete.setOnClickListener {
            reminder?.id?.let { it1 -> reminderRepository.delete(it1) }
            finish()
            val intent = Intent(this, IndexActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        reminder?.let {
            val reminderLocation = LatLng(it.latitude, it.longitude)
            mMap.addMarker(MarkerOptions().position(reminderLocation))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reminderLocation, 15f))
        }
    }
}