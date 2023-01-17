package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.KanjiSymbol
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.KanjiSymbolReading
import javax.xml.parsers.DocumentBuilderFactory

class SymbolsParserKanji {

    private companion object {
        private const val KANJI_FILE_LOCATION = "kanji/kanji.xml"
    }

    private fun parseSymbolReading(readingsText : String, kanjiSymbol : KanjiSymbol) {
        val lines = readingsText.split("\n")
        val japaneseText = lines[0]
        val romajiText = lines[1]
        val japaneseWords = japaneseText.split("、")
        val romajiWords = romajiText.split(", ")
        for (x in japaneseWords.indices) {
            val symbolReading = KanjiSymbolReading(
                japanese = japaneseWords[x],
                romaji = romajiWords[x]
            )
            kanjiSymbol.reading.add(symbolReading)
        }
    }

    fun get(context : Context) : List<KanjiSymbol> {
        val kanjiSymbols = ArrayList<KanjiSymbol>()
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        context.assets.open(KANJI_FILE_LOCATION).use { inputStream ->
            val document = documentBuilder.parse(inputStream)
            val rootElement = document.documentElement
            val children = rootElement.getElementsByTagName("row")
            for (x in 0 until children.length) {
                val child = children.item(x)
                val childNodes = child.childNodes
                val kanjiSymbol = KanjiSymbol(
                    symbolNew = childNodes.item(1).textContent,
                    symbolOld = childNodes.item(3).textContent,
                    radical = childNodes.item(5).textContent,
                    strokes = childNodes.item(7).textContent.toShortOrNull(),
                    grade = childNodes.item(9).textContent,
                    yearAdded = childNodes.item(11).textContent.toShortOrNull(),
                    englishMeaning = childNodes.item(13).textContent,
                )
                this.parseSymbolReading(childNodes.item(15).textContent, kanjiSymbol)
                kanjiSymbols.add(kanjiSymbol)
            }
        }
        return kanjiSymbols
    }

}