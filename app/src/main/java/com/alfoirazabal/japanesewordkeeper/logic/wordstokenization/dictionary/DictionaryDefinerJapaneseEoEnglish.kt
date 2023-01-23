package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.DictionaryJapaneseToEnglish
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition

class DictionaryDefinerJapaneseEoEnglish(val context : Context) {

    fun define(word : String) : List<Definition> {
        val definitions = DictionaryJapaneseToEnglish.getDefinitions(this.context)
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