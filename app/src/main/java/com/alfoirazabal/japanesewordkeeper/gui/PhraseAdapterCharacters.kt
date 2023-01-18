package com.alfoirazabal.japanesewordkeeper.gui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.SymbolsParser
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.domain.fetched.*
import com.alfoirazabal.japanesewordkeeper.logic.symbolsparsing.parsers.KanjiSymbolReadingParser

class PhraseAdapterCharacters : RecyclerView.Adapter<PhraseAdapterCharacters.ViewHolder>() {

    private lateinit var fetchedSymbols : FetchedSymbols
    private var orderedSymbols : MutableList<IFetchedSymbol> = ArrayList()

    private val kanjiSymbolReadingParser = KanjiSymbolReadingParser()

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val txtSymbol : TextView
        val txtKatakanaHiraganaPronunciation : TextView
        val layoutKanji : LinearLayout
        val txtOldSymbol : TextView
        val txtRadical : TextView
        val txtStrokes : TextView
        val txtGrade : TextView
        val txtYearAdded : TextView
        val txtEnglishMeaning : TextView
        val txtReading : TextView

        init {
            txtSymbol = view.findViewById(R.id.txt_symbol)
            txtKatakanaHiraganaPronunciation =
                view.findViewById(R.id.txt_katakana_hiragana_pronunciation)
            layoutKanji = view.findViewById(R.id.layout_kanji)
            txtOldSymbol = view.findViewById(R.id.txt_kanji_old_symbol)
            txtRadical = view.findViewById(R.id.txt_kanji_radical)
            txtStrokes = view.findViewById(R.id.txt_kanji_strokes)
            txtGrade = view.findViewById(R.id.txt_kanji_grade)
            txtYearAdded = view.findViewById(R.id.txt_kanji_year_added)
            txtEnglishMeaning = view.findViewById(R.id.txt_kanji_english_meaning)
            txtReading = view.findViewById(R.id.txt_kanji_reading)
        }

    }

    fun setPhrase(phrase : String, context : Context) {
        this.fetchedSymbols = SymbolsParser.parseSymbols(phrase, context)
        this.orderedSymbols.clear()
        this.orderedSymbols.addAll(this.fetchedSymbols.hiragana)
        this.orderedSymbols.addAll(this.fetchedSymbols.katakana)
        this.orderedSymbols.addAll(this.fetchedSymbols.kanji)
        this.orderedSymbols.sortBy { e -> e.order }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_symbols_overview, viewGroup, false)
        return ViewHolder(view)
    }

    private fun bindKanji(fetchedKanjiSymbol : FetchedKanjiSymbol, holder : ViewHolder) {
        holder.txtSymbol.setTextAppearance(R.style.Theme_JapaneseWordKeeper_Characters_Kanji)
        holder.txtSymbol.text = fetchedKanjiSymbol.symbol.symbolNew
        holder.txtOldSymbol.text = fetchedKanjiSymbol.symbol.symbolOld
        holder.txtRadical.text = fetchedKanjiSymbol.symbol.radical
        if (fetchedKanjiSymbol.symbol.strokes != null) {
            holder.txtStrokes.text = fetchedKanjiSymbol.symbol.strokes.toString()
        } else {
            holder.txtStrokes.text = ""
        }
        holder.txtGrade.text = fetchedKanjiSymbol.symbol.grade
        if (fetchedKanjiSymbol.symbol.yearAdded != null) {
            holder.txtYearAdded.text = fetchedKanjiSymbol.symbol.yearAdded.toString()
        } else {
            holder.txtYearAdded.text = ""
        }
        holder.txtEnglishMeaning.text = fetchedKanjiSymbol.symbol.englishMeaning
        holder.txtReading.text = this.kanjiSymbolReadingParser.parse(fetchedKanjiSymbol.symbol)
        holder.txtKatakanaHiraganaPronunciation.visibility = View.GONE
        holder.layoutKanji.visibility = View.VISIBLE
    }

    private fun bindKatakana(fetchedKatakanaSymbol: FetchedKatakanaSymbol, holder: ViewHolder) {
        holder.txtSymbol.setTextAppearance(R.style.Theme_JapaneseWordKeeper_Characters_Katakana)
        holder.txtSymbol.text = fetchedKatakanaSymbol.symbol.symbol
        holder.txtKatakanaHiraganaPronunciation.text = fetchedKatakanaSymbol.symbol.pronunciation
        holder.layoutKanji.visibility = View.GONE
        holder.txtKatakanaHiraganaPronunciation.visibility = View.VISIBLE
    }

    private fun bindHiragana(fetchedHiraganaSymbol: FetchedHiraganaSymbol, holder: ViewHolder) {
        holder.txtSymbol.setTextAppearance(R.style.Theme_JapaneseWordKeeper_Characters_Hiragana)
        holder.txtSymbol.text = fetchedHiraganaSymbol.symbol.symbol
        holder.txtKatakanaHiraganaPronunciation.text = fetchedHiraganaSymbol.symbol.pronunciation
        holder.layoutKanji.visibility = View.GONE
        holder.txtKatakanaHiraganaPronunciation.visibility = View.VISIBLE
    }

    private fun bindUnknownSymbol(fetchedUnknownSymbol: FetchedUnknownSymbol, holder: ViewHolder) {
        holder.txtSymbol.setTextAppearance(androidx.appcompat.R.style.Widget_AppCompat_TextView)
        holder.txtSymbol.text = fetchedUnknownSymbol.symbol
        holder.txtKatakanaHiraganaPronunciation.text = ""
        holder.layoutKanji.visibility = View.GONE
        holder.txtKatakanaHiraganaPronunciation.visibility = View.VISIBLE
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val symbol = this.orderedSymbols[position]
        var kanji : FetchedKanjiSymbol? = null
        var hiragana : FetchedHiraganaSymbol? = null
        var katakana : FetchedKatakanaSymbol? = null
        var unknownSymbol : FetchedUnknownSymbol? = null

        kanji = this.fetchedSymbols.kanji.find { e -> e.order == symbol.order }
        if (kanji != null) {
            this.bindKanji(kanji, holder)
        } else {
            hiragana = this.fetchedSymbols.hiragana.find { e -> e.order == symbol.order }
        }
        if (hiragana != null) {
            this.bindHiragana(hiragana, holder)
        } else {
            katakana = this.fetchedSymbols.katakana.find { e -> e.order == symbol.order }
        }
        if (katakana != null) {
            this.bindKatakana(katakana, holder)
        } else {
            unknownSymbol = this.fetchedSymbols.unknown.find { e -> e.order == symbol.order }
        }
        if (unknownSymbol != null) {
            this.bindUnknownSymbol(unknownSymbol, holder)
        }
    }

    override fun getItemCount(): Int {
        return this.orderedSymbols.size
    }

}