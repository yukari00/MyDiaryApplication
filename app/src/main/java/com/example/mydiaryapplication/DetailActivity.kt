package com.example.mydiaryapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        menu?.apply {
            findItem(R.id.menu_signout).isVisible = false
            findItem(R.id.menu_edit).isVisible = true
            findItem(R.id.menu_done).isVisible = false
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit -> {
                //Todo データを持って編集画面へ
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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

        fun getLaunchIntent(from: Context, data: NoteDataList) =
            Intent(from, DetailActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, data.date)
                putExtra(INTENT_KEY_TITLE, data.title)
                putExtra(INTENT_KEY_DETAIL, data.detail)
            }

    }
}
