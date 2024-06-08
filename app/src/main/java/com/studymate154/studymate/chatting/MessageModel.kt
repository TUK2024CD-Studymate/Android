package com.studymate154.studymate.chatting

data class MessageModel(
    var sender: String? = null,
    var content: String? = null,
    var chatRoomId: String? = null

)