package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.parsers

import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.KanjiSymbol

class KanjiSymbolReadingParser {

    fun parse(kanjiSymbol: KanjiSymbol) : String {
        val readings = kanjiSymbol.reading
        val romajis = ArrayList<String>()
        var japaneses = ArrayList<String>()
        for (reading in readings) {
            romajis.add(reading.romaji)
            japaneses.add(reading.japanese)
        }
        val parsedBuilder = StringBuilder()
        val romajisIterator = romajis.iterator()
        while (romajisIterator.hasNext()) {
            parsedBuilder.append(romajisIterator.next())
            if (romajisIterator.hasNext()) {
                parsedBuilder.append(", ")
            } else {
                parsedBuilder.append("\n")
            }
        }
        val japanesesIterator = japaneses.iterator()
        while (japanesesIterator.hasNext()) {
            parsedBuilder.append(japanesesIterator.next())
            if (japanesesIterator.hasNext()) {
                parsedBuilder.append("、")
            }
        }
        return parsedBuilder.toString()
    }

}