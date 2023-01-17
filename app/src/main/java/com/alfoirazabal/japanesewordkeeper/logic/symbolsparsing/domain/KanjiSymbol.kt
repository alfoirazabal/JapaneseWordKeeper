package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain

class KanjiSymbol(
    val symbolNew : String = "",
    val symbolOld : String = "",
    val radical : String = "",
    val strokes : Short? = null,
    val grade : String = "",
    val yearAdded : Short? = null,
    val englishMeaning : String = "",
    val reading : MutableList<KanjiSymbolReading> = ArrayList()
)