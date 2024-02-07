package com.example.studymate.signUp

data class LoginBackendResponse(
    val code : String,
    //200: 성공, 300,400: 에러
    val message : String,
    val token : String
)