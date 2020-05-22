package com.example.mydiaryapplication



class NoteDataList(val date : String?, val title : String?, val detail : String?, val id : String?)

class NoteData(val date : String?, val title : String?, val detail : String?){
    companion object {
        const val KEY_DATE = "date"
        const val KEY_TITLE = "title"
        const val KEY_DETAIL = "detail"
    }
}