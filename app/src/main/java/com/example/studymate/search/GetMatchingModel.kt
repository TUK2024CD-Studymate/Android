package com.example.studymate.search


data class GetMatchingModel (
    var id: String,
    var name: String,
    var nickname: String,
    var part: String,
    var email: String,
    var interests: String,
    var blogUrl: String,
    var publicRelations: String,
    var job: String,
    var heart: Int,
    var starAverage: Double, // 변경된 부분
    var solved: Int,
    var matchingCount: Int

)