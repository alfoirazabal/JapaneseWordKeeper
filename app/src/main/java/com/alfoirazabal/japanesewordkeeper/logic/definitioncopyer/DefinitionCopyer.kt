package com.alfoirazabal.japanesewordkeeper.logic.definitioncopyer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.romaji.HiraKataRomajinizer
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition

class DefinitionCopyer : IDefinitionCopyer {

    private val hiraKataRomajinizer = HiraKataRomajinizer()

    override fun copyToClipboard(context: Context, definition: Definition) {
        val word = definition.word
        val pronunciation = definition.pronunciation
        val romaji = hiraKataRomajinizer.romajinize(pronunciation)
        val text = "$word [$pronunciation = $romaji]"
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as
                ClipboardManager
        val clipData = ClipData.newPlainText(context.getString(R.string.definition), text)
        clipboardManager.setPrimaryClip(clipData)
    }

}