package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.fetched

class FetchedSymbols(
    val hiragana : List<FetchedHiraganaSymbol>,
    val katakana : List<FetchedKatakanaSymbol>,
    val kanji : List<FetchedKanjiSymbol>,
    val unknown : List<FetchedUnknownSymbol>
)