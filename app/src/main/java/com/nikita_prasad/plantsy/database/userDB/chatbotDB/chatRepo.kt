package com.nikita_prasad.plantsy.database.userDB.chatbotDB

class chatRepo(private val chatDAO: chatDAO, private val  messageDAO: messageDAO) {

    suspend fun readChatHistory(): List<chatEntity>{
        return chatDAO.readChats()
    }

    suspend fun addChat(chatEntity: chatEntity){
        chatDAO.addChat(chatEntity)
    }

    suspend fun getChatCount(): Int{
        return chatDAO.getChatCount()
    }

    suspend fun addMessages(messageEntity: messageEntity){
        messageDAO.addChat(messageEntity)
    }

    suspend fun deleteChatwMessage(userIndex: Int){
        chatDAO.getChatIDwUserIndex(userIndex = userIndex).forEach {
            messageDAO.deleteMessages(it)
        }
        chatDAO.deleteChat(userIndex)
    }

    suspend fun readChats(chatID: Int): List<chatWithMessage> {
        return chatDAO.getChatWithMessages(chatID)
    }

}