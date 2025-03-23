package com.nikita_prasad.plantsy.database.userDB.chatbotDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "messages"
)
data class messageEntity(@PrimaryKey(autoGenerate = true)
                         val id: Int = 0,
                         val message: String,
                         val isBotMessage: Boolean,
                         val timestamp: Long,
                         val isError: Boolean,
                         val chatId: String,
                         @ColumnInfo(name = "hasAttachments", defaultValue = "false")
                         val hasAttachments: Boolean = false, // contains timestamp as foreign key for retrieving scan_history data
                         @ColumnInfo(name = "isSearchEnabled", defaultValue = "false")
                         val isSearchEnabled: Boolean= false)
