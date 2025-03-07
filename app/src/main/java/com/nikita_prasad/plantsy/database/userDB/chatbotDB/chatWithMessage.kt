package com.nikita_prasad.plantsy.database.userDB.chatbotDB

import androidx.room.Embedded
import androidx.room.Relation

data class chatWithMessage(
@Embedded
val chat: chatEntity,
@Relation(parentColumn = "id", entityColumn = "chatId")
val messages: List<messageEntity>
)
