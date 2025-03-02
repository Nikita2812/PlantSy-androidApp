package com.nikita_prasad.plantsy.utils.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.nikita_prasad.plantsy.screen.chatbot.chatbot_dc
import com.nikita_prasad.plantsy.screen.chatbot.chatbot_obj
import com.nikita_prasad.plantsy.screen.chatbot.chatbot_obj.chatService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.IOException

private val defaultService = object {
    val serviceName = "swasthai"
    val secretKey = "swasthai"
}

class ChatVM : ViewModel() {

    private val _isChatbotResponding = MutableStateFlow(false)
    val isChatbotResponding: StateFlow<Boolean> = _isChatbotResponding

    suspend fun chat(message: String,
                     onSuccess: (chatbot_dc) -> Unit,
                     onError: (String) -> Unit,
                     mode: Int, //0 -> qna, 1 -> ai_symptoms_checker
                     isSearchEnabled: Boolean=false)
    {
        try{
            _isChatbotResponding.value = true

            val rawResponse = when(mode){
                0, 2 -> {
                    val response = chatService.sendMessage(
                        serviceName = defaultService.serviceName.lowercase(),
                        serviceCode = defaultService.secretKey,
                        message = message,
                        isSearchEnabled = isSearchEnabled
                    )
                    Log.d("chatDebug","message1")
                    response
                }

                1 -> {
                    val response = chatService.getChatReply(url = "https://api-jjtysweprq-el.a.run.app/symptoms/$message")
                    Log.d("chatDebug","message2")
                    response
                }

                3, 4, 5 -> {
                    val response = chatService.sendTaskMsg(
                        serviceName = defaultService.serviceName.lowercase(),
                        secretCode = defaultService.secretKey,
                        message = message,
                        task = mode
                    )
                    Log.d("chatDebug","message3")
                    response
                }
                else -> {
                    val response = chatService.getChatReply(url = "https://api-jjtysweprq-el.a.run.app/symptoms/$message")
                    Log.d("chatDebug","message4")
                    response

                }

            }
            onSuccess(rawResponse)
        }
        catch (e: Exception) {
            when (e) {
                is retrofit2.HttpException -> {
                    val errorMessage = when (e.code()) {
                        504 -> "Server timeout: Please try again later"
                        500 -> "Server error: Something went wrong"
                        404 -> "Service not found"
                        401 -> "Authentication failed"
                        else -> "Connection error: ${e.code()}"
                    }
                    Log.e("PlantsChatDebug", "HTTP error: $errorMessage")
                    onError(errorMessage)
                }
                is IOException -> {
                    Log.e("PlantsChatDebug", "Network error: ${e.message}")
                    onError("Network error: Please check your connection")
                }
                else -> {
                    Log.e("PlantsChatDebug", "Unknown error: ${e.message}")
                    onError("Something went wrong, please try again")
                }
            }
        }

            finally{
                _isChatbotResponding.value = false
            }
    }
}