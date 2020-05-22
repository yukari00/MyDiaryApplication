package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    private var id: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        if (intent.extras == null) {
            Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
            finish()
        }

        val bundle = intent.extras!!
        id = bundle.getString(INTENT_KEY_ID)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.apply {
            findItem(R.id.menu_signout).isVisible = false
            findItem(R.id.menu_back).isVisible = true
            findItem(R.id.menu_edit).isVisible = true
            findItem(R.id.menu_done).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_back -> {
                finish()
                return true
            }
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
        startActivity(EditActivity.getLaunchIntent(this,  id, STATUS_EDIT))
    }

    private fun update() {
        val database = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val docRef = database.collection("users").document(userId)
            .collection("notes").document(id!!)

        docRef.get().addOnSuccessListener {
            val date = it[NoteData.KEY_DATE] as String
            val title = it[NoteData.KEY_TITLE] as String
            val detail = it[NoteData.KEY_DETAIL] as String

            detail_date.text = date
            detail_title.text = title
            detail_detail.text = detail
        }

    }

    companion object {

        private const val INTENT_KEY_ID = "INTENT_KEY_ID"
        private const val STATUS_EDIT = "STATUS_EDIT"

        fun getLaunchIntent(from: Context, data: NoteDataList) =
            Intent(from, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, data.date)
                putExtra(INTENT_KEY_TITLE, data.title)
                putExtra(INTENT_KEY_DETAIL, data.detail)
                putExtra(INTENT_KEY_ID, data.id)
            }

    }
}
