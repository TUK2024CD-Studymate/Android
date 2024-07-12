package com.studymate154.studymate.Model


data class GetMatchingModel (
    var id: String,
    var name: String,
    var nickname: String,
    var part: String,
    var email: String,
    var interests: String,
    var tel : String,
    var blogUrl: String,
    var publicRelations: String,
    var job: String,
    var heart: Int,
    var starAverage: Double, // 변경된 부분
    var solved: Int?,
    var imageUrl: String?,
    var matchingCount: Int,
    var reviewCount : Int,
    var matchingPercent : Double

)