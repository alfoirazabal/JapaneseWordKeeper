package com.alfoirazabal.japanesewordkeeper.db.dao

import androidx.room.*
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase

@Dao
interface PhraseDAO {

    @Query("SELECT * FROM Phrase")
    fun getAll() : MutableList<Phrase>

    @Query("SELECT * FROM Phrase WHERE id = :id")
    fun getById(id : String) : Phrase

    @Update
    fun update(phrase : Phrase)

    @Insert
    fun insert(phrase : Phrase)

    @Delete
    fun delete(phrase : Phrase)

}