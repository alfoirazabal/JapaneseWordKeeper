package com.alfoirazabal.japanesewordkeeper.logic.definitioncopyer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.romaji.HiraKataRomajinizer
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition

class DefinitionAndMeaningsCopyer : IDefinitionCopyer {

    private val hiraKataRomajinizer = HiraKataRomajinizer()

    override fun copyToClipboard(context: Context, definition: Definition) {
        val word = definition.word
        val pronunciation = definition.pronunciation
        val romaji = hiraKataRomajinizer.romajinize(pronunciation)
        val definitionsStringBuilder = StringBuilder()
        var x = 0
        while (x < definition.definitions.size) {
            val subDefinition = definition.definitions[x]
            definitionsStringBuilder.append(subDefinition)
            if (x != definition.definitions.size - 1) {
                definitionsStringBuilder.append("\n")
            }
            x++
        }
        val definitions = definitionsStringBuilder.toString()
        val text = "$word [$pronunciation] [$romaji]\n$definitions"
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as
                ClipboardManager
        val clipData = ClipData.newPlainText(context.getString(R.string.definition), text)
        clipboardManager.setPrimaryClip(clipData)
    }

}