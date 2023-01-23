package com.alfoirazabal.japanesewordkeeper.logic.definitioncopyer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition

class DefinitionWordCopyer : IDefinitionCopyer {

    override fun copyToClipboard(context: Context, definition: Definition) {
        val text = definition.word
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as
                ClipboardManager
        val clipData = ClipData.newPlainText(context.getString(R.string.word), text)
        clipboardManager.setPrimaryClip(clipData)
    }

}