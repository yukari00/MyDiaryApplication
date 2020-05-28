package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_calendar.*
import kotlinx.android.synthetic.main.list_item_calendar.*
import kotlinx.android.synthetic.main.list_item_calendar_header.*
import java.text.SimpleDateFormat
import java.time.Clock.offset
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private var offsetMonth = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)

        val context = applicationContext ?: return

        val calendarAdapter = CalendarAdapter(object : CalendarAdapter.OnClickCalendarListener {
            override fun OnClick(item: CalendarItem.Day) {
                val date = item.date
                if (date != null) {
                    startActivity(DetailActivity.getLaunchIntent(this@CalendarActivity, date))

                }
            }
        })
        calendar_recycler_view.apply {
            adapter = calendarAdapter
            layoutManager = GridLayoutManager(context, 7)
        }
        calendarAdapter.dataSource = CalendarItemFactory.create(offsetMonth)
        updateDateLabel()

        pre_button.setOnClickListener {
            calendarAdapter.dataSource = CalendarItemFactory.create(--offsetMonth)
            updateDateLabel()
        }

        next_button.setOnClickListener {
            calendarAdapter.dataSource = CalendarItemFactory.create(++offsetMonth)
            updateDateLabel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_calendar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_signout -> {
                signOut()
                return true
            }
            R.id.menu_list -> {
                startActivity(ListActivity.getLaunchIntent(this))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateDateLabel() {
        text_date.text = Date().apply {
            offset(month = offsetMonth)
        }.toYearMonthText()
    }

    private fun Date.offset(year: Int = 0, month: Int = 0, day: Int = 0) {
        time = Calendar.getInstance().apply {
            add(Calendar.YEAR, year)
            add(Calendar.MONTH, month)
            add(Calendar.DATE, day)
        }.timeInMillis
    }

    private fun Date.toYearMonthText(): String {
        return SimpleDateFormat("yyyy/MM").format(time)
    }

    private fun signOut() {
        startActivity(MainActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut();
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, CalendarActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}

