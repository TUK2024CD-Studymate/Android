package com.studymate154.studymate.Model

data class ReviewVO (
    var title : String? = null,
    var content : String? = null,
    var star : Int? = null,
    var isSolved : Boolean? = null,
    var heart : Boolean? = null
        )