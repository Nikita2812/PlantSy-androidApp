package com.nikita_prasad.plantsy.database.userDB.chatbotDB

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class chatDB_VM(application: Application): AndroidViewModel(application) {

    private val repo: chatRepo
    private val dao: chatDAO
    private val messagesDA0: messageDAO

    init {
        dao = userDB.getUserDBRefence(application).chatDAO()
        messagesDA0 = userDB.getUserDBRefence(application).messageDAO()
        repo = chatRepo(dao, messagesDA0)
    }

    suspend fun addChat(chatEntity: chatEntity){
        viewModelScope.launch {
            repo.addChat(chatEntity)
        }
    }

    suspend fun addMessages(messageEntity: messageEntity){
        viewModelScope.launch {
            repo.addMessages(messageEntity)
        }
    }

    suspend fun readChatHistory(): List<chatEntity> {
        return withContext(viewModelScope.coroutineContext){
            repo.readChatHistory()
        }
    }

    suspend fun getChatCounts(): Int{
        return withContext(viewModelScope.coroutineContext){
            repo.getChatCount()
        }
    }

    suspend fun readChatWithMessages(chatid: Int): List<chatWithMessage>{
        return withContext(viewModelScope.coroutineContext){
            repo.readChats(chatid)
        }
    }

    suspend fun deleteChats(userIndex: Int){
        viewModelScope.launch {
            repo.deleteChatwMessage(userIndex)
        }
    }

}
