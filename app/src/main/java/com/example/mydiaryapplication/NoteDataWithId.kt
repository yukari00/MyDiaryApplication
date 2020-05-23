package com.example.mydiaryapplication

class NoteDataWithId(val date: String?, val title: String?, val detail: String?, val id: String?) {

    companion object {
        const val KEY_DATE = "date"
        const val KEY_TITLE = "title"
        const val KEY_DETAIL = "detail"
    }

}