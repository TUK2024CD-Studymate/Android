package com.studymate154.studymate.search

import com.studymate154.studymate.Model.ReviewModel

data class ReviewModelResponse (
        val reviewResponses: List<ReviewModel>,
        val status: String
        )