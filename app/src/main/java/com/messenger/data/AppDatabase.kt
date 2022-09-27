package com.messenger.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [
    Contacts::class,
    PrimaryUser::class,
    Conversations::class,
    Msgs::class
], version = 3, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun dao(): DAO

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "noctua_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}