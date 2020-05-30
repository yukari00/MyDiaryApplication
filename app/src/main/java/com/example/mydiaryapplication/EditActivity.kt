package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.mydiaryapplication.databinding.ActivityEditBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {

    private var status: Status? = null
    private var date: Date? = null

    private val database = FirebaseFirestore.getInstance()
    private lateinit var binding: ActivityEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityEditBinding>(this, R.layout.activity_edit)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val bundle = intent.extras
        date = bundle?.get(INTENT_KEY_DATE) as Date?
        status = bundle?.get(INTENT_KEY_STATUS) as Status

        if (date == null) {
            toast(getString(R.string.error))
            finish()
        }

        if (status == Status.EDIT) {
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            val docRef = database.collection(COLLECTION_USERS).document(userId)
                .collection(COLLECTION_NOTES).document(getId(date!!))

            docRef.get().addOnSuccessListener {
                val title = it.getString(NoteData.KEY_TITLE)
                val detail = it.getString(NoteData.KEY_DETAIL)

                binding.inputEditTitle.setText(title)
                binding.inputEditDetail.setText(detail)
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
                if (isFilled()) {
                    save()
                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun save() {

        val title = binding.inputEditTitle.text.toString()
        val detail = binding.inputEditDetail.text.toString()

        when (status) {
            Status.NEW_ENTRY -> addNewData(title, detail)
            Status.EDIT -> edit(title, detail)
        }
    }

    private fun isFilled(): Boolean {

        if (binding.inputEditTitle.text.toString() == "") {
            binding.inputTitle.error = getString(R.string.enter_something)
            return false
        }
        if (binding.inputEditDetail.text.toString() == "") {
            binding.inputDetail.error = getString(R.string.enter_something)
            return false
        }
        return true
    }

    private fun addNewData(title: String, detail: String) {

        val user = FirebaseAuth.getInstance().currentUser!!.uid

        val newData = NoteData(date, title, detail)
        database.collection(COLLECTION_USERS).document(user).collection(COLLECTION_NOTES)
            .document(getId(date!!)).set(newData)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
                toast(getString(R.string.save_success))
                finish()

            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error writing document", e)
                toast(getString(R.string.save_failure))
            }
    }

    private fun edit(title: String, detail: String) {

        val user = FirebaseAuth.getInstance().currentUser!!.uid

        val newData = NoteData(date, title, detail)

        database.collection(COLLECTION_USERS).document(user).collection(COLLECTION_NOTES)
            .document(getId(date!!)).set(newData).addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
                toast(getString(R.string.save_success))
                finish()

            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error writing document", e)
                toast(getString(R.string.save_failure))
            }

    }

    private fun toast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    companion object {

        private const val INTENT_KEY_DATE = "INTENT_KEY_DATE"
        private const val INTENT_KEY_STATUS = "INTENT_KEY_STATUS"

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"

        fun getLaunchIntent(context: Context, date: Date?, status: Status) =
            Intent(context, EditActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, date)
                putExtra(INTENT_KEY_STATUS, status)
            }

        fun getId(date: Date): String {
            val dateFormat = SimpleDateFormat("yyyy.MM.dd")
            val dateString = dateFormat.format(date)

            return dateString
        }

    }

}
