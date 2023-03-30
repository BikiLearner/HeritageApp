package com.example.hritage.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hritage.model.DocumentLidtClassEntity

@Database(entities = [DocumentLidtClassEntity::class], version = 1)
abstract class DatabaseForFileDataBase:RoomDatabase() {
    abstract fun databaseForListDao():DatabaseForFileListDao

    companion object{
        private var INSTANCE:DatabaseForFileDataBase?=null

        fun getInstance(context: Context):DatabaseForFileDataBase{

            if(INSTANCE==null) {
                synchronized(this) {
                    INSTANCE =Room.databaseBuilder(context.applicationContext
                    ,DatabaseForFileDataBase::class.java,"doc_name").build()
                }
            }
            return INSTANCE!!
        }
    }
}