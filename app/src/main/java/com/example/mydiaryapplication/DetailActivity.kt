package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.mydiaryapplication.databinding.ActivityDetailBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class DetailActivity : AppCompatActivity() {

    private var date: Date? = null
    private var status: Status? = null
    private lateinit var binding: ActivityDetailBinding

    private var noteData: NoteData? = null

    private val database = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView<ActivityDetailBinding>(this, R.layout.activity_detail)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        date = bundle?.get(INTENT_KEY_DATE) as Date?

        if (date == null) {
            Toast.makeText(this, getString(R.string.error), Toast.LENGTH_LONG).show()
            finish()
        }
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
            R.id.menu_delete ->{
                if(noteData?.title != null && noteData?.detail != null) {
                    deleteData(noteData!!)
                }else {
                    toast(getString(R.string.delete_no_data_error))
                }
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

        startActivity(EditActivity.getLaunchIntent(this, date, status!!))

    }

    private fun update() {

        val docRef = database.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_NOTES).document(EditActivity.getId(date!!))

        docRef.get().addOnSuccessListener {
            val title = it.getString(NoteData.KEY_TITLE)
            val detail = it.getString(NoteData.KEY_DETAIL)

            if (title == null && detail == null) {
                status = Status.NEW_ENTRY
            } else {
                status = Status.EDIT
            }

            noteData = NoteData(date, title, detail)
            val noteDataString = NoteDataString(EditActivity.getId(date!!), title, detail)
            binding.noteDataString = noteDataString

        }

    }

    private fun deleteData(data: NoteData) {

        AlertDialog.Builder(this).apply {
            setTitle(getString(R.string.delete_title))
            setMessage(getString(R.string.delete_message))
            setPositiveButton(getString(R.string.yes)) { dialog, which ->
                setPositiveButton(data)
            }
            setNegativeButton(getString(R.string.no)) { dialog, which -> }
            show()
        }
    }

    private fun setPositiveButton(data: NoteData) {
        database.collection(COLLECTION_USERS).document(userId)
            .collection(COLLECTION_NOTES).document(EditActivity.getId(data.date!!)).delete()
            .addOnSuccessListener {
                toast(getString(R.string.delete_success_toast))
                update()
            }.addOnFailureListener {
                toast(getString(R.string.delete_failure_toast))
            }
    }

    private fun toast(message: String) {
        Toast.makeText(this@DetailActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val INTENT_KEY_DATE = "INTENT_KEY_DATE"

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"

        fun getLaunchIntent(context: Context, data: NoteData) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, data.date)
            }

        fun getLaunchIntent(context: Context, date: Date?) =
            Intent(context, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, date)
            }

    }
}
