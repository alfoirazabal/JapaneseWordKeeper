package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.HiraganaSymbol
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.KanjiSymbol
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.KatakanaSymbol
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.fetched.*

object SymbolsParser {

    private var initialized = false

    private val kanji = ArrayList<KanjiSymbol>()
    private val hiragana = ArrayList<HiraganaSymbol>()
    private val katakana = ArrayList<KatakanaSymbol>()
    private val ignorableCharacters = arrayListOf("、", "。", "ー", "\n", "\r", " ", "")

    fun parseSymbols(phrase : String, context : Context) : FetchedSymbols {
        if (!initialized) {
            kanji.addAll(SymbolsParserKanji().get(context))
            hiragana.addAll(SymbolsParserHiragana().get())
            katakana.addAll(SymbolsParserKatakana().get())
            initialized = true
        }
        val fetchedHiragana = ArrayList<FetchedHiraganaSymbol>()
        val fetchedKanji = ArrayList<FetchedKanjiSymbol>()
        val fetchedKatakana = ArrayList<FetchedKatakanaSymbol>()
        val unknownSymbols = ArrayList<FetchedUnknownSymbol>()
        val phraseCharacters = phrase.split("")
        var ignoredCharactersCount = 0
        for (x in phraseCharacters.indices) {
            val order = x - ignoredCharactersCount
            var found = ignorableCharacters.contains(phraseCharacters[x])
            if (found) {
                ignoredCharactersCount++
            }
            if (!found) {
                val hiraganaSymbol = hiragana.find { e -> e.symbol == phraseCharacters[x] }
                if (hiraganaSymbol != null) {
                    found = true
                    fetchedHiragana.add(FetchedHiraganaSymbol(order, hiraganaSymbol))
                }
            }
            if (!found) {
                val katakanaSymbol = katakana.find { e -> e.symbol == phraseCharacters[x] }
                if (katakanaSymbol != null) {
                    found = true
                    fetchedKatakana.add(FetchedKatakanaSymbol(order, katakanaSymbol))
                }
            }
            if (!found) {
                val kanjiSymbol = kanji.find { e -> e.symbolNew == phraseCharacters[x] }
                if (kanjiSymbol != null) {
                    found = true
                    fetchedKanji.add(FetchedKanjiSymbol(order, kanjiSymbol))
                }
            }
            if (!found) {
                unknownSymbols.add(FetchedUnknownSymbol(order, phraseCharacters[x]))
            }
        }
        return FetchedSymbols(fetchedHiragana, fetchedKatakana, fetchedKanji, unknownSymbols)
    }

}