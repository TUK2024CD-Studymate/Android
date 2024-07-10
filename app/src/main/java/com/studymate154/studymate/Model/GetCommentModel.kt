package com.studymate154.studymate.Model

data class GetCommentModel(
    var id: String? = null,
    var content: String? = null,
    var nickname: String? = null,
    var post_id: String? = null,
    var createdAt : String? = null
)