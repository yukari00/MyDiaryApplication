package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mydiaryapplication.databinding.ActivityListBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ListActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var binding : ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)

        setSupportActionBar(binding.toolbar)

        binding.floatingButton.setOnClickListener {
            startActivity(EditActivity.getLaunchIntent(this, Date(), Status.NEW_ENTRY))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_calendar-> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()

        showList()
    }

    private fun showList() {

        val list: MutableList<DocumentSnapshot>? = mutableListOf()
        database.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_NOTES)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CHECK THIS", "${document.id} => ${document.data}")
                    list?.add(document)
                }
                val newList = list?.map {
                    NoteData(
                        it.getDate(NoteData.KEY_DATE),
                        it.getString(NoteData.KEY_TITLE),
                        it.getString(NoteData.KEY_DETAIL)
                    )
                }

                if (newList != null) {
                    val myAdapter = MyAdapter(newList,
                        object : MyAdapter.OnClickNoteListener {
                            override fun OnClick(data: NoteData) {
                                val intent = DetailActivity.getLaunchIntent(this@ListActivity, data)
                                startActivity(intent)
                            }

                            override fun OnLongClick(data: NoteData) {
                                deleteData(data)
                            }
                        })
                    val manager = LinearLayoutManager(this)
                    binding.recyclerView.apply {
                        setHasFixedSize(true)
                        layoutManager = manager
                        adapter = myAdapter
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("CHECK THIS", "Error getting documents: ", exception)
            }

    }

    private fun deleteData(data: NoteData) {

        AlertDialog.Builder(this@ListActivity).apply {
            setTitle(getString(R.string.delete_title))
            setMessage(getString(R.string.delete_message))
            setPositiveButton(getString(R.string.yes)) { dialog, which ->
                database.collection(COLLECTION_USERS).document(userId)
                    .collection(COLLECTION_NOTES).document(EditActivity.getId(data.date!!)).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this@ListActivity, getString(R.string.delete_success_toast), Toast.LENGTH_SHORT).show()
                        showList()
                    }.addOnFailureListener {
                        Toast.makeText(this@ListActivity, getString(R.string.delete_failure_toast), Toast.LENGTH_SHORT).show()
                    }
            }
            setNegativeButton(getString(R.string.no)) { dialog, which -> }
            show()
        }
    }

    private fun signOut() {
        startActivity(MainActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut();
    }

    companion object {

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"

        fun getLaunchIntent(from: Context) = Intent(from, ListActivity::class.java)
    }
}
