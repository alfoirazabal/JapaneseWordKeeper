package com.alfoirazabal.japanesewordkeeper.gui.guihelpers

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase

class PhraseLanguageCommons(val context : Context, val phrase : Phrase) {

    fun getFlagEmoji() : String {
        val emoji : String = when (phrase.textLanguage) {
            "es" -> {
                context.getString(R.string.emoji_spanish_flag)
            }
            "en" -> {
                context.getString(R.string.emoji_us_flag)
            }
            else -> {
                throw Error("Unknown language: " + phrase.textLanguage)
            }
        }
        return emoji
    }

}