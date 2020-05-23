package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_list.*

class ListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        showList()
        floating_button.setOnClickListener {
            startActivity(EditActivity.getLaunchIntent(this))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.apply {
            findItem(R.id.menu_signout).isVisible = true
            findItem(R.id.menu_edit).isVisible = false
            findItem(R.id.menu_done).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_signout -> {
                signOut()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun showList() {
        val database = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val list: MutableList<DocumentSnapshot>? = mutableListOf()
        database.collection("users").document(userId).collection("notes")
            .get().addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("CHECK THIS", "${document.id} => ${document.data}")
                    list?.add(document)
                }
                val newList = list?.map {
                    NoteDataList(
                        it.getString(NoteDataList.KEY_DATE), it.getString(NoteDataList.KEY_TITLE),
                        it.getString(NoteDataList.KEY_DETAIL), it.id
                    )
                }

                if (newList != null) {
                    val myAdapter = MyAdapter(newList,
                        object : MyAdapter.OnClickNoteListener {
                            override fun OnClick(data: NoteDataList) {
                                val intent = DetailActivity.getLaunchIntent(this@ListActivity, data)
                                startActivity(intent)
                            }

                            override fun OnLongClick(data: NoteDataList) {
                                deleteData(data)
                            }
                        })
                    val manager = LinearLayoutManager(this)
                    recycler_view.apply {
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

    private fun deleteData(data: NoteDataList) {
        val database = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        AlertDialog.Builder(this@ListActivity).apply {
            setTitle("削除")
            setMessage("削除してもいいですか")
            setPositiveButton("はい") { dialog, which ->
                val a = database.collection("users").document(userId)
                    .collection("notes").document(data.id!!).delete()
                    .addOnSuccessListener {
                        Toast.makeText(this@ListActivity, "削除されました", Toast.LENGTH_SHORT).show()
                        showList()
                    }.addOnFailureListener {
                        Toast.makeText(this@ListActivity, "削除されませんでした", Toast.LENGTH_SHORT).show()
                    }
            }
            setNegativeButton("いいえ") { dialog, which -> }
            show()
        }
    }

    private fun signOut() {
        startActivity(MainActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut();
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, ListActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}
