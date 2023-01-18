package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.fetched

import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.KanjiSymbol

class FetchedKanjiSymbol(override val order: Int, val symbol : KanjiSymbol) : IFetchedSymbol