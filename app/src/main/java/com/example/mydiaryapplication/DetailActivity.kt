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

class DetailActivity : AppCompatActivity() {

    private var id: String? = null
    private lateinit var binding : ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.extras == null) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
            finish()
        }

        val bundle = intent.extras!!
        id = bundle.getString(INTENT_KEY_ID)

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
        startActivity(EditActivity.getLaunchIntent(this, id))
    }

    private fun update() {
        val database = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val docRef = database.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_NOTES).document(id!!)

        docRef.get().addOnSuccessListener {
            val date = it[NoteDataWithId.KEY_DATE] as String
            val title = it[NoteDataWithId.KEY_TITLE] as String
            val detail = it[NoteDataWithId.KEY_DETAIL] as String

            val noteData = NoteData(date, title, detail)
            binding.noteData = noteData

        }

    }

    companion object {

        private const val INTENT_KEY_ID = "INTENT_KEY_ID"

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"

        fun getLaunchIntent(from: Context, data: NoteDataWithId) =
            Intent(from, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_ID, data.id)
            }

    }
}
