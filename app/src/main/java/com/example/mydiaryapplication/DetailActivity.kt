package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mydiaryapplication.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class DetailActivity : AppCompatActivity() {

    private var date: Date? = null
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.extras == null) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
            finish()
        }

        val bundle = intent.extras!!
        date = bundle.get(INTENT_KEY_DATE) as Date

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                goEdit()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        update()
    }

    private fun goEdit() {
        startActivity(EditActivity.getLaunchIntent(this, date))
    }

    private fun update() {
        val database = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val docRef = database.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_NOTES).document(date.toString())

        docRef.get().addOnSuccessListener {
            val title = it[NoteData.KEY_TITLE] as String?
            val detail = it[NoteData.KEY_DETAIL] as String?

            val noteData = NoteData(date, title, detail)
            binding.noteData = noteData

        }

    }

    companion object {

        private const val INTENT_KEY_DATE = "INTENT_KEY_DATE"

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"

        fun getLaunchIntent(from: Context, data: NoteData) =
            Intent(from, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, data.date)
            }

        fun getLaunchIntent(from: Context, date: Date?) =
            Intent(from, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, date)
            }

    }
}
