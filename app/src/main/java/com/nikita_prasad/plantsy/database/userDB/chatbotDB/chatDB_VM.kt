package com.nikita_prasad.plantsy.database.userDB.chatbotDB

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class chatDB_VM(application: Application): AndroidViewModel(application) {

    private val repo: chatRepo
    private val dao: chatDAO
    private val messagesDAO: messageDAO
    init {
        dao = userDB.getUserDBRefence(application).chatDAO()
        messagesDAO = userDB.getUserDBRefence(application).messageDAO()
        repo = chatRepo(dao, messagesDAO)
    }
    suspend fun addChat(chatEntity: chatEntity) {
        return withContext(Dispatchers.IO) {
            repo.addChat(chatEntity)
        }
    }
    suspend fun addMessages(messageEntity: messageEntity) {
        return withContext(Dispatchers.IO) {
            repo.addMessages(messageEntity)
        }
    }
    suspend fun readChatHistory(): List<chatEntity> {
        return withContext(Dispatchers.IO) {
            repo.readChatHistory()
        }
    }
    suspend fun getChatCounts(): Int {
        return withContext(Dispatchers.IO) {
            repo.getChatCount()
        }
    }
    suspend fun readChatWithMessages(chatId: Int): List<chatWithMessage> {
        return withContext(Dispatchers.IO) {
            repo.readChats(chatId)
        }
    }

    suspend fun deleteChats(userIndex: Int) {
        withContext(Dispatchers.IO) {
            repo.deleteChatwMessage(userIndex)
        }
    }
}
