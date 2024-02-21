package com.example.studymate.board

data class GetCommentModel(
    var id: String? = null,
    var content: String? = null,
    var nickname: String? = null,
    var post_id: String? = null
)