package com.example.studymate.chatting

data class ReviewVO (
    var title : String? = null,
    var content : String? = null,
    var star : Int? = null,
    var isSolved : Boolean? = null,
    var heart : Boolean? = null
        )