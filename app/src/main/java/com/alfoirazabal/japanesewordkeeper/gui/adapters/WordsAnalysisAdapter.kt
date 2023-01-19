package com.alfoirazabal.japanesewordkeeper.gui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.JWKTokenizer

class WordsAnalysisAdapter : RecyclerView.Adapter<WordsAnalysisAdapter.ViewHolder>() {

    private var words : List<JWKTokenizer.Word> = ArrayList()

    lateinit var phrase : Phrase
        private set

    fun setPhrase(phrase: Phrase, context : Context) {
        val tokenizer = JWKTokenizer(context)
        this.words = tokenizer.fetchWords(phrase.translation)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val txtOrder : TextView
        val txtJapaneseWord : TextView
        val recyclerViewWordMeanings : RecyclerView

        init {
            txtOrder = view.findViewById(R.id.txt_order)
            txtJapaneseWord = view.findViewById(R.id.txt_japanese_word)
            recyclerViewWordMeanings = view.findViewById(R.id.recyclerview_japanese_word_meanings)
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_japanese_word, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val word = this.words[position]
        holder.txtOrder.text = (position + 1).toString()
        holder.txtJapaneseWord.text = word.japanese
    }

    override fun getItemCount(): Int {
        return this.words.size
    }
}