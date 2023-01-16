package com.alfoirazabal.japanesewordkeeper.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.alfoirazabal.japanesewordkeeper.db.dao.PhraseDAO
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase

@Database(
    entities = [
        Phrase::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun phrasesDAO() : PhraseDAO
}