package com.nikita_prasad.plantsy.database.userDB.chatbotDB

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [
         chatEntity::class, messageEntity::class
    ],
    version = 7,
    exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 6, to = 7)
    ]
)
abstract class userDB: RoomDatabase() {


    abstract fun messageDAO(): messageDAO
    abstract fun chatDAO(): chatDAO

    companion object{
        @Volatile
        private var INSTANCE: userDB? = null

        fun getUserDBRefence(context: Context): userDB {
            val getUserDBtemp = INSTANCE
            if (getUserDBtemp!=null){
                return getUserDBtemp
            }
            synchronized(this){
                val instanceUserDB= Room.databaseBuilder(
                    context.applicationContext,
                    userDB::class.java,
                    "userDB"
                )
                    .build()
                INSTANCE= instanceUserDB
                return instanceUserDB
            }
        }
    }
}