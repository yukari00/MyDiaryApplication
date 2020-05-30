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

class ListActivity : AppCompatActivity() {

    private val database = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid
    private lateinit var binding: ActivityListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityListBinding>(this, R.layout.activity_list)

        setSupportActionBar(binding.toolbar)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_calendar -> {
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
        val snapshotList: MutableList<DocumentSnapshot> = mutableListOf()
        database.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_NOTES)
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CHECK THIS", "${document.id} => ${document.data}")
                    snapshotList.add(document)
                }
                connectRecyclerView(snapshotList)
            }
            .addOnFailureListener { exception ->
                Log.d("CHECK THIS", "Error getting documents: ", exception)
            }
    }

    private fun connectRecyclerView(snapshotList: MutableList<DocumentSnapshot>) {
        val noteDataList = snapshotList.map {
            NoteData(
                it.getDate(NoteData.KEY_DATE),
                it.getString(NoteData.KEY_TITLE),
                it.getString(NoteData.KEY_DETAIL)
            )
        }
        val myAdapter = MyAdapter(noteDataList,
            object : MyAdapter.OnClickNoteListener {
                override fun OnClick(data: NoteData) {
                    startActivity(DetailActivity.getLaunchIntent(this@ListActivity, data))
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

    private fun deleteData(data: NoteData) {

        AlertDialog.Builder(this@ListActivity).apply {
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
                showList()
            }.addOnFailureListener {
                toast(getString(R.string.delete_failure_toast))
            }
    }

    private fun toast(message: String) {
        Toast.makeText(this@ListActivity, message, Toast.LENGTH_SHORT).show()
    }

    companion object {

        private const val COLLECTION_USERS = "users"
        private const val COLLECTION_NOTES = "notes"

        fun getLaunchIntent(context: Context) = Intent(context, ListActivity::class.java)
    }
}
