package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mydiaryapplication.databinding.ActivityCalendarBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity() {

    private var offsetMonth = 0

    private val database = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    private lateinit var binding : ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityCalendarBinding>(this, R.layout.activity_calendar)
        setSupportActionBar(binding.toolbar)

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

    override fun onResume() {
        super.onResume()

        update()
    }

    private fun update() {

        val snapshotList: MutableList<DocumentSnapshot> = mutableListOf()
        database.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_NOTES)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CHECK THIS", "${document.id} => ${document.data}")
                    snapshotList.add(document)
                }
                val noteDataList = snapshotList.map {
                    NoteData(
                        it.getDate(NoteData.KEY_DATE),
                        it.getString(NoteData.KEY_TITLE),
                        it.getString(NoteData.KEY_DETAIL)
                    )
                }

                val calendarAdapter =
                    CalendarAdapter(noteDataList, object : CalendarAdapter.OnClickCalendarListener {
                        override fun OnClick(item: CalendarItem.Day) {
                            goDetail(item)
                        }
                    })
                binding.calendarRecyclerView.apply {
                    adapter = calendarAdapter
                    layoutManager = GridLayoutManager(this@CalendarActivity, 7)
                }
                calendarAdapter.dataSource = CalendarItemFactory.create(offsetMonth)
                updateDateLabel()

                binding.preButton.setOnClickListener {
                    calendarAdapter.dataSource = CalendarItemFactory.create(--offsetMonth)
                    updateDateLabel()
                }

                binding.nextButton.setOnClickListener {
                    calendarAdapter.dataSource = CalendarItemFactory.create(++offsetMonth)
                    updateDateLabel()
                }
            }

    }

    private fun goDetail(item: CalendarItem.Day) {
        val date = item.date
        if (date != null) {
            startActivity(DetailActivity.getLaunchIntent(this@CalendarActivity, date))
        }
    }

    private fun updateDateLabel() {
        binding.textDate.text = Date().apply {
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

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"

        fun getLaunchIntent(context: Context) = Intent(context, CalendarActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}

