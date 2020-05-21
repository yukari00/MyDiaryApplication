package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    var date = ""
    var title = ""
    var detail = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val bundle = intent.extras!!
        date = bundle.getString(INTENT_KEY_DATE)!!
        title = bundle.getString(INTENT_KEY_TITLE)!!
        detail = bundle.getString(INTENT_KEY_DETAIL)!!

        upDate()
    }

    private fun upDate() {
        detail_date.text = date
        detail_title.text = title
        detail_detail.text = detail
    }

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, DetailActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }

    }
}
