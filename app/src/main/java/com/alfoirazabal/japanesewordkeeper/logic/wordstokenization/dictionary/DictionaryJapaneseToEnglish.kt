package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.EDICTDefinitionBuilderHelper
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition
import java.io.BufferedReader
import java.io.InputStreamReader

object DictionaryJapaneseToEnglish {

    private var definitions : MutableList<Definition>? = null

    var onDictionaryLoading : () -> Unit = { }
    var onDictionaryLoaded : () -> Unit = { }

    private fun buildDictionary(context : Context) {
        this.definitions = ArrayList()
        val definitionBuilderHelper = EDICTDefinitionBuilderHelper()
        context.assets.open("dictionaries/edict").use { inputStream ->
            InputStreamReader(inputStream, "EUC-JP").use { inputStreamReader ->
                BufferedReader(inputStreamReader).use { bufferedReader ->
                    var firstLineIndex : Int
                    bufferedReader.lines().forEach { line ->
                        firstLineIndex = line.indexOf("/")
                        val japanesePart = line.substring(0, firstLineIndex)
                        val japaneseParts = definitionBuilderHelper.parseJapaneseParts(japanesePart)
                        val definitionPart = line.substring(firstLineIndex + 1)
                        val definitions = definitionBuilderHelper.parseDefinitions(definitionPart)
                        val definition = Definition(
                            word = japaneseParts[0],
                            pronunciation = japaneseParts[1],
                            definitions = definitions
                        )
                        this.definitions!!.add(definition)
                    }
                }
            }
        }
    }

    fun getDefinitions(context : Context) : List<Definition> {
        if (this.definitions == null) {
            this.onDictionaryLoading.invoke()
            this.buildDictionary(context)
        }
        this.onDictionaryLoaded.invoke()
        return this.definitions!!
    }

}