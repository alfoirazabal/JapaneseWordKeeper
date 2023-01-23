package com.alfoirazabal.japanesewordkeeper.logic.definitioncopyer

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition

class DefinitionPronunciationCopyer : IDefinitionCopyer {

    override fun copyToClipboard(context: Context, definition: Definition) {
        val text = definition.pronunciation
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as
                ClipboardManager
        val clipData = ClipData.newPlainText(context.getString(R.string.pronunciation), text)
        clipboardManager.setPrimaryClip(clipData)
    }

}