package com.example.remy.database.entities

data class Reminder(
    val title: String,
    val description: String,
    val date: String,
    val time: String,
    val location: String,
    val latitude: Double,
    val longitude: Double
) {
    var id: Int = 0

    constructor(
        id: Int,
        title: String,
        description: String,
        date: String,
        time: String,
        location: String,
        latitude: Double,
        longitude: Double
    ) : this(title, description, date, time, location, latitude, longitude) {
        this.id = id
    }

    override fun toString(): String {
        return "Reminder(id=$id, title='$title', description='$description', date='$date', time='$time', location='$location', latitude=$latitude, longitude=$longitude)"
    }
}
