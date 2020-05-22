package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if(intent.extras == null){
            Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
            finish()
        }

        val bundle = intent.extras!!
        val date = bundle.getString(INTENT_KEY_DATE)!!
        val title = bundle.getString(INTENT_KEY_TITLE)!!
        val detail = bundle.getString(INTENT_KEY_DETAIL)!!

        update(date, title, detail)
    }

    private fun update(date: String, title: String, detail: String) {
        detail_date.text = date
        detail_title.text = title
        detail_detail.text = detail
    }

    companion object {

        private const val INTENT_KEY_DATE = "INTENT_KEY_DATE"
        private const val INTENT_KEY_TITLE = "INTENT_KEY_TITLE"
        private const val INTENT_KEY_DETAIL = "INTENT_KEY_DETAIL"

        fun getLaunchIntent(from: Context, data: NoteData) =
            Intent(from, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, data.date)
                putExtra(INTENT_KEY_TITLE, data.title)
                putExtra(INTENT_KEY_DETAIL, data.detail)

                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

    }
}
