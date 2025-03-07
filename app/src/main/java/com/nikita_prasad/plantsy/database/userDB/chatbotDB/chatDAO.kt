package com.nikita_prasad.plantsy.database.userDB.chatbotDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface chatDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChat(chatEntity: chatEntity)

    @Query("select * from chats order by timestamp desc")
    suspend fun readChats(): List<chatEntity>

    @Transaction
    @Query("select * from chats where id = :chatId")
    suspend fun getChatWithMessages(chatId: Int): List<chatWithMessage>

    @Query("select count(*) from chats")
    suspend fun getChatCount(): Int

    @Transaction
    @Query("delete from chats where userIndex like :userIndex")
    suspend fun deleteChat(userIndex: Int)

    @Query("select id from chats where userIndex like :userIndex")
    suspend fun getChatIDwUserIndex(userIndex: Int): List<Int>
}