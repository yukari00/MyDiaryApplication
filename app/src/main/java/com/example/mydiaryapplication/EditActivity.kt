package com.example.mydiaryapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val bundle = intent.extras

        //ボタンはToolbar作成後、修正
        button_edit_done.setOnClickListener {
            addNewData()
        }

    }

    private fun addNewData() {

        val database = FirebaseFirestore.getInstance()

        val date = "2020年5月19日"
        val setDetail = input_edit_detail.text.toString()

        if(setDetail == ""){
            inputDetail.error = "入力してください"
            return
        }

        val user = FirebaseAuth.getInstance().currentUser!!.uid

        val newData = hashMapOf<String, String>("date" to date, "detail" to setDetail)
        database.collection("users").document(user).collection("notes").add(newData)
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully written!")
                Toast.makeText(this, "登録が完了しました", Toast.LENGTH_LONG).show()
                finish()

            }
            .addOnFailureListener {
                    e -> Log.w("TAG", "Error writing document", e)
                Toast.makeText(this, "登録ができませんでした", Toast.LENGTH_LONG).show()
            }
    }

}
