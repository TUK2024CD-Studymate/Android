package com.example.studymate.signUp

data class UserModel(
    var email : String ?= null,
    var password : String ?= null,
    var fcmToken : String ?= null,
)