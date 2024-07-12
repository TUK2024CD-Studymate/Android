package com.studymate154.studymate.Model

data class GetMessageModel (
    var messageId: String? = null,
    var sender: String? = null,
    var content: String? = null,
    var sendDate: String? = null,
    var profileImageUrl: String? = null,
    var read : Boolean
        )