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
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class EditActivity : AppCompatActivity(), OnMapReadyCallback {

    // Declaração das variáveis
    private lateinit var mMap: GoogleMap
    private var reminder: Reminder? = null
    private var selectedLatLng: LatLng? = null
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialização do mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Obtenção dos dados do lembrete
        val id = intent.getIntExtra("id", 0)
        if (id == 0) finish()

        val reminderRepository = ReminderRepository(this)
        reminder = reminderRepository.show(id)

        // Configuração dos campos de texto com os dados do lembrete
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

        // Configuração do botão de salvar
        val btnSave = findViewById<Button>(R.id.save)
        btnSave.setOnClickListener {
            if (title.text.isEmpty() || description.text.isEmpty() || date.text.isEmpty() || time.text.isEmpty() || location.text.isEmpty()) {
                return@setOnClickListener
            }

            // Atualização dos dados do lembrete
            val updatedReminder = Reminder(
                id,
                title.text.toString(),
                description.text.toString(),
                date.text.toString(),
                time.text.toString(),
                location.text.toString(),
                selectedLatLng?.latitude ?: 0.0,
                selectedLatLng?.longitude ?: 0.0
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
            marker = mMap.addMarker(MarkerOptions().position(reminderLocation))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(reminderLocation, 15f))
        }

        // Configurar o listener de clique no mapa
        mMap.setOnMapClickListener { latLng ->
            // Atualizar a latitude e longitude selecionadas
            selectedLatLng = latLng

            // Atualizar a posição do marcador no mapa
            marker?.position = latLng
        }
    }
}
