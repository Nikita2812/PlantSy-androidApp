package com.nikita_prasad.plantsy.screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nikita_prasad.plantsy.screen.chatbot.chatbot_dc
import com.nikita_prasad.plantsy.utils.viewmodel.ChatVM
import kotlinx.coroutines.launch

@Composable
fun ChatbotScreen(modifier: Modifier = Modifier) {
    val chatVM: ChatVM = viewModel()
    val coroutineScope = rememberCoroutineScope()

    // State for user input and chat history
    var userInput by remember { mutableStateOf("") }
    var response by remember { mutableStateOf(chatbot_dc("")) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            if (isLoading) {
                Box(modifier = Modifier, contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (response.message?.isNotEmpty() == true) {
                response.message?.let { Text(text = it) }
            } else {
                Text(text = "Type a message to start chatting about plant care!")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type your message...") },
                singleLine = true
            )


            Button(
                onClick = {
                    if (userInput.isNotEmpty() && !isLoading) {
                        isLoading = true
                        val messageToSend = userInput

                        Log.d("ChatbotScreen", "Sending message: $messageToSend")

                        userInput = ""

                        coroutineScope.launch {
                            chatVM.chat(
                                message = messageToSend,
                                onSuccess = { result ->
                                    response = result
                                    isLoading = false
                                    Log.d("response", "Received chatbot message: $result")
                                },
                                onError = { error ->
                                    response.message = "Error: $error"
                                    isLoading = false
                                    Log.d("response", "Error: $error")
                                },
                                mode = 0,
                                isSearchEnabled = false
                            )
                        }
                    }
                },
                enabled = userInput.isNotEmpty() && !isLoading
            ) {
                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send message"
                )
            }
        }
    }
}