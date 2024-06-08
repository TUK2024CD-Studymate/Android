package com.studymate154.studymate.chatting

data class Member(
    val id: Int,
    val name: String,
    val nickname: String,
    val expertiseField: String,
    val interests: List<String>,
    val login: Boolean,
    val profileImageUrl : String
)

data class ChatRoom(
    val chatRoomId: Int,
    val chatRoomName: String,
    val members: List<Member>,
    val unreadMessageCount : Int
)