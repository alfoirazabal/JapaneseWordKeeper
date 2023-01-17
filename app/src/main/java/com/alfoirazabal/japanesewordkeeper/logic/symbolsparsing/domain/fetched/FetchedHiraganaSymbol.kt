package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.fetched

import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.HiraganaSymbol

class FetchedHiraganaSymbol(override val order: Int, val symbol : HiraganaSymbol) : IFetchedSymbol