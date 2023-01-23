package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.DictionaryJapaneseToEnglish
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition
import java.util.concurrent.atomic.AtomicBoolean

class DictionarySearcherJapaneseEnglish(val context : Context) {

    var onSearchProgress : (definitionsScanned : Int, definitionsTotal : Int) -> Unit = { _, _ -> }
    var onResultFound : (definition : Definition) -> Unit = { }
    var onFinished : () -> Unit = { }

    var onLoadingDictionary : () -> Unit = { }
    var onLoadedDictionary : () -> Unit = { }

    fun search(word : String, stopSearch : AtomicBoolean) {
        val wordLC = word.lowercase()
        DictionaryJapaneseToEnglish.onDictionaryLoading = {
            this.onLoadingDictionary.invoke()
        }
        DictionaryJapaneseToEnglish.onDictionaryLoaded = {
            this.onLoadedDictionary.invoke()
        }
        val definitions = DictionaryJapaneseToEnglish.getDefinitions(context)
        val definitionsTotal = definitions.size
        var definitionsScanned = 0
        for (definition in definitions) {
            if (stopSearch.get()) {
                break
            }
            val definitionIsFound =
                definition.word == wordLC ||
                definition.pronunciation == wordLC ||
                definition.definitions.any { subDefinition -> subDefinition.lowercase().contains(wordLC) }
            if (definitionIsFound) {
                this.onResultFound.invoke(definition)
            }
            definitionsScanned++
            this.onSearchProgress.invoke(definitionsScanned, definitionsTotal)
        }
        this.onFinished.invoke()
    }

}