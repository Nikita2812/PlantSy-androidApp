package com.nikita_prasad.plantsy.database.appDB

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nikita_prasad.plantsy.database.appDB.diseaseInfo.DiseaseDC
import com.nikita_prasad.plantsy.database.appDB.diseaseInfo.diseaseDAO

@Database(
    entities = [DiseaseDC::class],
    version = 1,
    exportSchema = true
)

abstract class appDataDB: RoomDatabase() {
    abstract fun diseaseDAO(): diseaseDAO

    companion object{
        @Volatile
        private var INSTANCE: appDataDB? = null

        fun getAppDBReference(context: Context): appDataDB{
            val getUserDBtemp = INSTANCE
            if (getUserDBtemp!=null){
                return getUserDBtemp
            }
            synchronized(this){
                val instanceUserDB = Room.databaseBuilder(
                    context.applicationContext,
                    appDataDB::class.java,
                    "appDataDB"
                ).build()
                INSTANCE = instanceUserDB
                return instanceUserDB
            }
        }
    }
}