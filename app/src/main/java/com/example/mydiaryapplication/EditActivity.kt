package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> {
                addNewData()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun addNewData() {

        val database = FirebaseFirestore.getInstance()

        val title = input_edit_title.text.toString()
        val detail = input_edit_detail.text.toString()

        if (title == "") {
            input_title.error = "入力してください"
            return
        }
        if (detail == "") {
            input_detail.error = "入力してください"
            return
        }

        val user = FirebaseAuth.getInstance().currentUser!!.uid
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日(E) HH:mm")
        val date = dateFormat.format(Date())

        val newData = NoteData(date, title, detail)
        database.collection("users").document(user).collection("notes").add(newData)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
                Toast.makeText(this, "登録が完了しました", Toast.LENGTH_LONG).show()
                finish()

            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error writing document", e)
                Toast.makeText(this, "登録ができませんでした", Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, EditActivity::class.java)
    }

}
