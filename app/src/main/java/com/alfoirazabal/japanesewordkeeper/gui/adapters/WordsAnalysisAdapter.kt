package com.alfoirazabal.japanesewordkeeper.gui.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase
import com.alfoirazabal.japanesewordkeeper.logic.romaji.HiraKataRomajinizer
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.JWKTokenizer

class WordsAnalysisAdapter : RecyclerView.Adapter<WordsAnalysisAdapter.ViewHolder>() {

    private var defaultOrderTextColor : Int = 0
    private var defaultJapaneseWordTextColor : Int = 0

    val highlitedWords : MutableList<JWKTokenizer.Word> = ArrayList()

    private val romajinizer = HiraKataRomajinizer()

    private var words : List<JWKTokenizer.Word> = ArrayList()

    lateinit var phrase : Phrase

    fun processPhrase(context : Context) {
        val tokenizer = JWKTokenizer(context)
        this.words = tokenizer.fetchWords(phrase.translation)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val txtOrder : TextView
        val txtJapaneseWord : TextView
        val layoutWordMeanings : LinearLayout

        init {
            txtOrder = view.findViewById(R.id.txt_order)
            txtJapaneseWord = view.findViewById(R.id.txt_japanese_word)
            layoutWordMeanings = view.findViewById(R.id.layout_word_meanings)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_japanese_word, viewGroup, false)
        defaultOrderTextColor =
            view.findViewById<TextView>(R.id.txt_order).currentTextColor
        defaultJapaneseWordTextColor =
            view.findViewById<TextView>(R.id.txt_japanese_word).currentTextColor
        return ViewHolder(view)
    }

    private fun printWordMeaning(
        holder : ViewHolder, word : JWKTokenizer.Word, meaning : JWKTokenizer.Meaning
    ) {
        val context = holder.layoutWordMeanings.context
        val romaji : String
        if (meaning.pronunciation != "") {
            romaji = romajinizer.romajinize(meaning.pronunciation)
        } else {
            romaji = romajinizer.romajinize(word.japanese)
        }
        val japanesePronunciation : String
        if (meaning.pronunciation != "") {
            japanesePronunciation = meaning.pronunciation
        } else {
            japanesePronunciation = word.japanese
        }
        val title = "$japanesePronunciation = $romaji"
        val txtTitle = TextView(context)
        txtTitle.gravity = Gravity.CENTER
        txtTitle.typeface = Typeface.DEFAULT_BOLD
        val txtMeaning = TextView(context)
        txtTitle.text = title
        val subMeaningsTextBuilder = StringBuilder()
        for (x in 0 until meaning.subMeanings.size) {
            subMeaningsTextBuilder.append(meaning.subMeanings[x])
            if (x != meaning.subMeanings.size - 1) {
                subMeaningsTextBuilder.append("\n")
            }
        }
        txtMeaning.text = subMeaningsTextBuilder.toString()
        holder.layoutWordMeanings.addView(txtTitle)
        holder.layoutWordMeanings.addView(txtMeaning)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = this.words[position]
        holder.txtOrder.text = (position + 1).toString()
        holder.txtJapaneseWord.text = word.japanese
        holder.layoutWordMeanings.removeAllViews()
        for (meaning in word.meanings) {
            this.printWordMeaning(holder, word, meaning)
        }
        val setWordHighlight = fun(_: View) {
            if (this.highlitedWords.contains(word)) {
                this.highlitedWords.remove(word)
            } else {
                this.highlitedWords.add(word)
            }
            this.notifyItemChanged(position)
        }
        holder.txtOrder.setOnClickListener(setWordHighlight)
        holder.txtJapaneseWord.setOnClickListener(setWordHighlight)
        if (this.highlitedWords.contains(word)) {
            holder.txtOrder.setTextColor(Color.RED)
            holder.txtJapaneseWord.setTextColor(Color.RED)
        } else {
            holder.txtOrder.setTextColor(defaultOrderTextColor)
            holder.txtJapaneseWord.setTextColor(defaultJapaneseWordTextColor)
        }
    }

    override fun getItemCount(): Int {
        return this.words.size
    }
}