package com.example.remy.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.remy.R
import com.example.remy.database.entities.Reminder

class ReminderAdapter(context: Context, private val reminders: List<Reminder>) : ArrayAdapter<Reminder>(context, R.layout.list_item_layout, reminders) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = View.inflate(context, R.layout.list_item_layout, null)
        }

        val reminder = reminders[position]
        itemView?.findViewById<TextView>(R.id.title_list)?.text = reminder.title
        itemView?.findViewById<TextView>(R.id.description_list)?.text = reminder.description

        val formattedDate = reminder.date.split("-").reversed().joinToString("/")
        itemView?.findViewById<TextView>(R.id.date_list)?.text = formattedDate

        val formattedTime = reminder.time.split(":").reversed().joinToString(":")
        itemView?.findViewById<TextView>(R.id.time_list)?.text = formattedTime

        val formattedLocation = "${reminder.location} (${reminder.latitude}, ${reminder.longitude})"
        itemView?.findViewById<TextView>(R.id.location_list)?.text = formattedLocation

        return itemView!!
    }
}