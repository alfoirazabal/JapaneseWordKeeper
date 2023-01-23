package com.alfoirazabal.japanesewordkeeper.logic.definitioncopyer

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition

interface IDefinitionCopyer {

    fun copyToClipboard(context : Context, definition: Definition)

}