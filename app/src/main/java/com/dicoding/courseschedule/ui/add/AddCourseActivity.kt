package com.dicoding.courseschedule.ui.add

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.ui.ViewModelFactory
import com.dicoding.courseschedule.util.TimePickerFragment
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*
class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var addViewModel: AddCourseViewModel
    private lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.add_course)

        val factory = ViewModelFactory.createFactory(this)
        addViewModel = ViewModelProvider(this, factory).get(AddCourseViewModel::class.java)

        addViewModel.saved.observe(this, {
            if (it.getContentIfNotHandled() == true)
                finish()
            else{
                val message = getString((R.string.input_empty_message))
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName = findViewById<TextInputEditText>(R.id.ed_add_course).text.toString().trim()
                val schdule = findViewById<Spinner>(R.id.schdule).selectedItemPosition
                val startTime = findViewById<TextView>(R.id.tv_time_startTime).text.toString().trim()
                val endTime = findViewById<TextView>(R.id.tv_time_endTime).text.toString().trim()
                val lecture = findViewById<TextInputEditText>(R.id.ed_lecture).text.toString().trim()
                val notes = findViewById<TextInputEditText>(R.id.ed_note).text.toString().trim()

                addViewModel.insertCourse(courseName, schdule, startTime, endTime, lecture, notes)
                true
            }
            else -> onOptionsItemSelected(item)
        }
    }

    fun showStartTimePicker(view: View){
        val startTimePicker = TimePickerFragment()
        startTimePicker.show(
            supportFragmentManager, "startTime"
        )
        this.view = view
    }

    fun showEndTimePicker(view: View){
        val endTimePicker = TimePickerFragment()
        endTimePicker.show(
            supportFragmentManager, "endTime"
        )
        this.view = view
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        when(view.id){
            R.id.btn_startTime -> {
                findViewById<TextView>(R.id.tv_time_startTime).text = timeFormat.format(calendar.time)
            }
            R.id.btn_endTime -> {
                findViewById<TextView>(R.id.tv_time_endTime).text = timeFormat.format(calendar.time)
            }
        }
    }
}