package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.EDICTDefinitionBuilderHelper
import java.io.BufferedReader
import java.io.InputStreamReader

class DictionaryJapaneseToEnglish(private var context: Context) {

    class Definition(
        val word : String,
        val pronunciation : String,
        val definitions : Array<String>
    )

    companion object {
        private var definitionsBuilt = false
        private lateinit var definitions : MutableList<Definition>
    }

    init {
        if (!definitionsBuilt) {
            buildDictionary()
        }
    }

    private fun buildDictionary() {
        definitions = ArrayList()
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
                        DictionaryJapaneseToEnglish.definitions.add(definition)
                    }
                }
            }
        }
        definitionsBuilt = true
    }

    fun define(word : String) : List<Definition> {
        val definitionsForWord = ArrayList<Definition>()
        var found = false
        for (definition in definitions) {
            if (definition.word == word || definition.word == "$word ") {
                definitionsForWord.add(definition)
                found = true
            } else {
                if (found) {
                    break
                }
            }
        }
        return definitionsForWord
    }

}