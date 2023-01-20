package com.alfoirazabal.japanesewordkeeper.logic.romaji

import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.SymbolsParserHiragana
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.SymbolsParserKatakana
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.NativeJapaneseSymbol
import java.util.concurrent.atomic.AtomicBoolean

class HiraKataRomajinizer() {

    companion object {
        private const val LONG_VOWEL_MARK = "ー"
        private const val SOKUON_HIRAGANA = "っ"
        private const val SOKUON_KATAKANA = "ッ"
    }

    private val japaneseDigraphs = ArrayList<NativeJapaneseSymbol>()
    private val japaneseSymbols = ArrayList<NativeJapaneseSymbol>()

    init {
        val parserHiragana = SymbolsParserHiragana()
        val parserKatakana = SymbolsParserKatakana()
        val hiraganaSymbols = parserHiragana.get()
        val katakanaSymbols = parserKatakana.get()
        val hiraganaDigraphs = parserHiragana.getDigraphs()
        val katakanaDigraphs = parserKatakana.getDigraphs()
        japaneseSymbols.addAll(hiraganaSymbols)
        japaneseSymbols.addAll(katakanaSymbols)
        japaneseDigraphs.addAll(hiraganaDigraphs)
        japaneseDigraphs.addAll(katakanaDigraphs)
    }

    private fun longVowelize(romajiSymbol : String) : String {
        val longVowelized : String
        val longVowel : Char
        if (romajiSymbol.isNotEmpty()) {
            when (romajiSymbol[romajiSymbol.length - 1]) {
                'a' -> {
                    longVowel = 'ā'
                }
                'e' -> {
                    longVowel = 'ē'
                }
                'i' -> {
                    longVowel = 'ī'
                }
                'o' -> {
                    longVowel = 'ō'
                }
                'u' -> {
                    longVowel = 'ū'
                }
                else -> {
                    longVowel = '?'
                }
            }
            longVowelized = romajiSymbol.substring(0, romajiSymbol.length - 1) + longVowel
        } else {
            longVowel = '?'
            longVowelized = longVowel.toString()
        }
        return longVowelized
    }

    fun romajinizeSymbol(
        symbol : String, romajiSymbols : MutableList<String>, foundSokuon : AtomicBoolean
    ) {
        if (symbol == LONG_VOWEL_MARK) {
            if (romajiSymbols.isNotEmpty()) {
                val lastRomajiSymbol = romajiSymbols.last()
                val longVowelizedRomajiSymbol = this.longVowelize(lastRomajiSymbol)
                romajiSymbols[romajiSymbols.lastIndex] = longVowelizedRomajiSymbol
            } else {
                romajiSymbols.add("?")
            }
        } else if (symbol == SOKUON_HIRAGANA || symbol == SOKUON_KATAKANA) {
            foundSokuon.set(true)
        } else {
            val foundJapaneseSymbol = japaneseSymbols.find { it.symbol == symbol }
            if (foundJapaneseSymbol != null) {
                var pronunciation = foundJapaneseSymbol.pronunciation
                if (pronunciation != "") {
                    if (foundSokuon.get()) {
                        val firstCharacter = pronunciation[0]
                        pronunciation = firstCharacter + pronunciation
                        foundSokuon.set(false)
                    }
                    romajiSymbols.add(pronunciation)
                } else {
                    if (foundSokuon.get()) foundSokuon.set(false)
                    romajiSymbols.add("?")
                }
            } else {
                romajiSymbols.add("?")
            }
        }
    }

    fun romajinize(word : String) : String {
        val romajiSymbols = ArrayList<String>()
        val allSymbols = word.split("")
        val symbols = allSymbols.filter { it != "" }
        var foundSokuon = AtomicBoolean(false)
        var digraphFound = false
        for (x in symbols.indices) {
            if (digraphFound) {
                digraphFound = false
                continue
            }
            if (x != symbols.size - 1) {
                val possibleDigraph = symbols[x] + symbols[x + 1]
                val foundDigraph = japaneseDigraphs.find { it.symbol == possibleDigraph }
                if (foundDigraph != null) {
                    romajiSymbols.add(foundDigraph.pronunciation)
                    digraphFound = true
                } else {
                    this.romajinizeSymbol(symbol = symbols[x], romajiSymbols, foundSokuon)
                }
            } else {
                this.romajinizeSymbol(symbol = symbols[x], romajiSymbols, foundSokuon)
            }
        }
        val stringBuilder = StringBuilder()
        for (x in 0 until romajiSymbols.size) {
            val romajiSymbol = romajiSymbols[x]
            stringBuilder.append(romajiSymbol)
            if (x != romajiSymbols.size - 1) {
                stringBuilder.append(" ")
            }
        }
        return stringBuilder.toString()
    }

}