package com.studymate154.studymate.Model

data class ReviewModel(
    var content: String,
    var writer: String,
    var star: Int,
    var createAt: String,
    var imageUrl : String
)