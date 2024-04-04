package com.example.studymate.chatting

data class MessageModel(
    var sender: String? = null,
    var message: String? = null,
    var type: String? = null,
    var roomId: String? = null,
)