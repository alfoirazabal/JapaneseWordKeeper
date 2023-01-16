package com.alfoirazabal.japanesewordkeeper.db

import android.content.Context
import androidx.room.Room
import java.io.File

object Database {

    private var appDatabase: AppDatabase? = null

    private fun getExternalDir(context: Context) : String {
        val externalPaths = context.getExternalFilesDirs("/")
        var lastExternalPath : File? = null
        externalPaths.forEach { externalPath ->
            if (externalPath != null) {
                lastExternalPath = externalPath
            }
        }
        if (lastExternalPath == null) {
            throw Error("Could not find where to save and load the DB")
        }
        val externalPath = lastExternalPath.toString()
        return externalPath + "AppDatabase.sqlite"
    }

    fun get(context : Context): AppDatabase {
        if (appDatabase == null) {
            val appDatabasePath = getExternalDir(context)
            appDatabase = Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                appDatabasePath
            ).build()
        }
        return appDatabase!!
    }

}