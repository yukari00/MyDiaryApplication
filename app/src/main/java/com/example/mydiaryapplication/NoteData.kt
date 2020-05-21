package com.example.mydiaryapplication



class NoteData(val date : String?, val title : String?, val detail : String?){
    companion object {
        const val KEY_DATE = "date"
        const val KEY_TITLE = "title"
        const val KEY_DETAIL = "detail"
    }
}