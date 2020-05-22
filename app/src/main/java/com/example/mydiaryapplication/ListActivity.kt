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

        setupUI()
        showList()
        floating_button.setOnClickListener {
            startActivity(EditActivity.getLaunchIntent(this))
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
                    NoteDataList(it.getString(NoteDataList.KEY_DATE), it.getString(NoteDataList.KEY_TITLE),
                        it.getString(NoteDataList.KEY_DETAIL), it.id)
                }

                Log.d("CHHHHHHH", "リストは$list")
                Log.d("CHHHHHHH", "ニューリストは$newList")

                if (newList != null) {
                    val viewAdapter = MyAdapter(newList,
                        object : MyAdapter.OnClickNoteListener {
                            override fun OnClick(data : NoteDataList){
                                val intent = DetailActivity.getLaunchIntent(this@ListActivity).apply {
                                    putExtra(INTENT_KEY_DATE, data.date)
                                    putExtra(INTENT_KEY_TITLE, data.title)
                                    putExtra(INTENT_KEY_DETAIL, data.detail)
                                }
                                startActivity(intent)
                            }
                            override fun OnLongClick(data : NoteDataList) {
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
                        })
                    val viewManager = LinearLayoutManager(this)
                    recycler_view.apply {
                        setHasFixedSize(true)
                        layoutManager = viewManager
                        adapter = viewAdapter
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.d("CHECK THIS", "Error getting documents: ", exception)
            }

    }

    private fun setupUI() {
        sign_out_button.setOnClickListener {
            signOut()
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
