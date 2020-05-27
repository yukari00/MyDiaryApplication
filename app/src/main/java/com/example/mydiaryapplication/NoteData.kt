package com.example.mydiaryapplication

import java.util.*

class NoteData(val date : Date?, val title : String?, val detail : String?){
    companion object {
        const val KEY_DATE = "date"
        const val KEY_TITLE = "title"
        const val KEY_DETAIL = "detail"
    }
}