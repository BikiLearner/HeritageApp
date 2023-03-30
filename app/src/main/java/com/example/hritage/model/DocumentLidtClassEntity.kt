package com.example.hritage.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "notes")
data class DocumentLidtClassEntity(
   @PrimaryKey(autoGenerate = true)
   val id:Int,
   @ColumnInfo(name = "Which")
    val ListId:String,
   @ColumnInfo(name = "File Name")
    val fileName:String,
)
