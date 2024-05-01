package com.example.remy

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import com.example.remy.repository.ReminderRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        requestLocationPermission()
        setupFusedLocationClient()
        getLastKnownLocation()

        setupUI()
    }

    private fun requestLocationPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Precise location access granted.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No location access granted.", Toast.LENGTH_LONG).show()
            }
        }
        locationPermissionRequest.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun setupFusedLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        currentLocation?.latitude = location.latitude
                        currentLocation?.longitude = location.longitude

                        Toast.makeText(
                            this,
                            "Latitude: ${location.latitude}, Longitude: ${location.longitude}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            checkDistanceOfReminders()
        }
    }

    private fun setupUI() {
        val btnIndex: Button = findViewById(R.id.index)
        val btnCreate: Button = findViewById(R.id.create)

        btnIndex.setOnClickListener {
            val intent = Intent(this, IndexActivity::class.java)
            startActivity(intent)
        }

        btnCreate.setOnClickListener {
            val intent = Intent(this, CreateActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkDistanceOfReminders() {
        val reminderRepository = ReminderRepository(this)
        val reminders = reminderRepository.index()

        if (reminders.isEmpty()) return

        for (reminder in reminders) {
            // Calculate distance between reminder and current location

            val reminderLocation = Location("reminder")
            reminderLocation.latitude = reminder.latitude
            reminderLocation.longitude = reminder.longitude

            val distance = currentLocation?.distanceTo(reminderLocation)
            if (distance != null && distance < 100) {
                // Notify user
                Toast.makeText(
                    this,
                    "You are near ${reminder.title}.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
