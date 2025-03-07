package com.nikita_prasad.plantsy.database.userDB.chatbotDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface messageDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addChat(messageEntity: messageEntity)

    @Query("delete from messages where chatId like :chatID")
    suspend fun deleteMessages(chatID: Int)
}