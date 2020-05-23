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

    private var status : String? = ""
    private var id : String? = ""

    lateinit var database : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(intent.extras == null){
            Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
            finish()
        }

        val bundle = intent.extras!!
        id = bundle.getString(INTENT_KEY_ID)
        status = bundle.getString(INTENT_KEY_STATUS)

        if (status == STATUS_EDIT) {
            val database = FirebaseFirestore.getInstance()
            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            val docRef = database.collection("users").document(userId)
                .collection("notes").document(id!!)

            docRef.get().addOnSuccessListener {
                val title = it[NoteData.KEY_TITLE] as String
                val detail = it[NoteData.KEY_DETAIL] as String

                input_edit_title.setText(title)
                input_edit_detail.setText(detail)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_done -> {
                checkIt()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun checkIt() : Boolean {
        database = FirebaseFirestore.getInstance()

        val title = input_edit_title.text.toString()
        val detail = input_edit_detail.text.toString()

        if (title == "") {
            input_title.error = "入力してください"
            return false
        }
        if (detail == "") {
            input_detail.error = "入力してください"
            return false
        }

        when(status){
            STATUS_NEW -> addNewData(title, detail)
            STATUS_EDIT -> edit(title, detail)
        }
        return true
    }

    private fun addNewData(title: String, detail: String) {

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

    private fun edit(title: String, detail: String) {

        val user = FirebaseAuth.getInstance().currentUser!!.uid
        val dateFormat = SimpleDateFormat("yyyy年MM月dd日(E) HH:mm")
        val date = dateFormat.format(Date())

        val newData = NoteData(date, title, detail)

        database.collection("users").document(user).collection("notes")
            .document(id!!).set(newData).addOnSuccessListener {
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

        private const val INTENT_KEY_ID = "INTENT_KEY_ID"
        private const val INTENT_KEY_STATUS = "INTENT_KEY_STATUS"

        private const val STATUS_NEW = "STATUS_NEW"
        private const val STATUS_EDIT = "STATUS_EDIT"

        fun getLaunchIntent(from: Context, id: String?, status: String?)
                = Intent(from, EditActivity::class.java).apply{
            putExtra(INTENT_KEY_STATUS, status)
            putExtra(INTENT_KEY_ID, id)
        }
    }

}
