package com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.fetched

import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.KatakanaSymbol

class FetchedKatakanaSymbol(override val order : Int, val symbol : KatakanaSymbol) : IFetchedSymbol