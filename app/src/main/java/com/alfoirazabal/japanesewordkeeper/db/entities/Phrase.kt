package com.alfoirazabal.japanesewordkeeper.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Phrase (

    @PrimaryKey val id : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "text_language") var textLanguage : String = "",
    @ColumnInfo(name = "text") var text : String = "",
    @ColumnInfo(name = "romaji") var romaji : String = "",
    @ColumnInfo(name = "translation") var translation : String = "",
    @ColumnInfo(name = "dateCreated") var dateCreated : Date = Date(),
    @ColumnInfo(name = "dateModified") var dateModified : Date = Date(),
    @ColumnInfo(name = "dateLastAccessed") var dateLastAccessed : Date = Date()

)