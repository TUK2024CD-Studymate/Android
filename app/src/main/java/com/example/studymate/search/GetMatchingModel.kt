package com.example.studymate.search

data class GetMatchingModel (
    var id: String? = null,
    var name: String? = null,
    var nickname: String? = null,
    var part: String? = null,
    var email: String? = null,
    var interests: String? = null,
    var blogUrl: String? = null,
    var publicRelations: String? = null,
    var job: String? = null,
    var heart: Int,
    var starAverage: Double, // 변경된 부분
    var solved: Int,
    var matchingCount: Int

)