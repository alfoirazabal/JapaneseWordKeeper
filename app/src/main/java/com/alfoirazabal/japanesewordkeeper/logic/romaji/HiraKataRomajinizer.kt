package com.alfoirazabal.japanesewordkeeper.logic.romaji

import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.SymbolsParserHiragana
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.SymbolsParserKatakana
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.NativeJapaneseSymbol

class HiraKataRomajinizer() {

    private val japaneseSymbols = ArrayList<NativeJapaneseSymbol>()

    init {
        val parserHiragana = SymbolsParserHiragana()
        val parserKatakana = SymbolsParserKatakana()
        val hiraganaSymbols = parserHiragana.get()
        val katakanaSymbols = parserKatakana.get()
        japaneseSymbols.addAll(hiraganaSymbols)
        japaneseSymbols.addAll(katakanaSymbols)
    }

    fun romajinize(word : String) : String {
        val stringBuilder = StringBuilder()
        val allSymbols = word.split("")
        val symbols = allSymbols.filter { it != "" }.toTypedArray()
        val symbolsIterator = symbols.iterator()
        while (symbolsIterator.hasNext()) {
            val symbol = symbolsIterator.next()
            val foundJapaneseSymbol = japaneseSymbols.find { it.symbol == symbol }
            val romajiForSymbol : String = foundJapaneseSymbol?.pronunciation ?: "?"
            stringBuilder.append(romajiForSymbol)
            if (symbolsIterator.hasNext()) {
                stringBuilder.append(" ")
            }
        }
        return stringBuilder.toString()
    }

}