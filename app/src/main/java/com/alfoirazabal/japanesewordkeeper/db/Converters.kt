package com.alfoirazabal.japanesewordkeeper.db

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun fromTimeStamp(value : Long?) : Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun toTimeStamp(date : Date?) : Long? {
        return date?.time
    }

}