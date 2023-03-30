package com.example.hritage.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hritage.model.DocumentLidtClassEntity

@Dao
interface DatabaseForFileListDao {
    @Insert
    suspend fun insertNote(noteModel: DocumentLidtClassEntity)

    @Query("DELETE FROM notes WHERE id = :id")
    suspend fun deleteNote(id:Int)

    @Query("SELECT * From notes")
    fun fetchAll(): LiveData<List<DocumentLidtClassEntity>>

    @Query("SELECT * From notes WHERE Which = :ListId")
    fun fetchContentByListId(ListId:String):LiveData<List<DocumentLidtClassEntity>>

}