package com.alfoirazabal.japanesewordkeeper.gui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase

class PhraseAdapter :
    RecyclerView.Adapter<PhraseAdapter.ViewHolder>() {

    enum class ORDER {
        DATE_CREATED_ASC,
        DATE_CREATED_DESC,
        DATE_MODIFIED_ASC,
        DATE_MODIFIED_DESC,
        DATE_LAST_ACCESSED_ASC,
        DATE_LAST_ACCESSED_DESC
    }

    private var searchQuery : String = ""

    private val phrases = ArrayList<Phrase>()
    private val searchedPhrases = ArrayList<Phrase>()

    var onPhraseClicked : (Phrase) -> Unit = { }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val txtTextLanguage : TextView
        val txtText : TextView
        val txtRomaji : TextView
        val txtTranslation : TextView

        init {
            txtTextLanguage = view.findViewById(R.id.txt_text_language)
            txtText = view.findViewById(R.id.txt_text)
            txtRomaji = view.findViewById(R.id.txt_romaji)
            txtTranslation = view.findViewById(R.id.txt_translation)
        }
    }

    fun setPhrases(phrases : List<Phrase>) {
        this.phrases.clear()
        this.searchedPhrases.clear()
        this.phrases.addAll(phrases)
        this.searchedPhrases.addAll(phrases)
    }

    fun sort(order : ORDER) {
        when (order) {
            ORDER.DATE_CREATED_ASC -> this.phrases.sortBy { e -> e.dateCreated }
            ORDER.DATE_CREATED_DESC -> this.phrases.sortByDescending { e -> e.dateCreated }
            ORDER.DATE_MODIFIED_ASC -> this.phrases.sortBy { e -> e.dateModified }
            ORDER.DATE_MODIFIED_DESC -> this.phrases.sortByDescending { e -> e.dateModified }
            ORDER.DATE_LAST_ACCESSED_ASC -> this.phrases.sortBy { e -> e.dateLastAccessed }
            ORDER.DATE_LAST_ACCESSED_DESC -> this.phrases.sortByDescending { e -> e.dateLastAccessed }
        }
        this.updateSearchQuery(this.searchQuery)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_view_phrase_simple, viewGroup, false)

        return ViewHolder(view)
    }

    private fun phraseIsSearched(phrase : Phrase) : Boolean {
        return phrase.text.lowercase().contains(this.searchQuery) ||
                phrase.romaji.lowercase().contains(this.searchQuery) ||
                phrase.translation.contains(this.searchQuery)
    }

    fun updateSearchQuery(searchQuery : String) {
        this.searchQuery = searchQuery.lowercase()
        this.searchedPhrases.clear()
        for (phrase in this.phrases) {
            if (this.phraseIsSearched(phrase)) {
                this.searchedPhrases.add(phrase)
            }
        }
        this.notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val phrase = searchedPhrases[position]
        when (phrase.textLanguage) {
            "en" -> {
                holder.txtTextLanguage.text = context.getString(R.string.text_language_en)
            }
            "es" -> {
                holder.txtTextLanguage.text = context.getString(R.string.text_language_es)
            }
            else -> {
                throw Error(
                    "Could not find text language for phrase language: " + phrase.textLanguage
                )
            }
        }
        holder.txtText.text = phrase.text
        holder.txtRomaji.text = phrase.romaji
        holder.txtTranslation.text = phrase.translation

        holder.itemView.setOnClickListener {
            this.onPhraseClicked.invoke(phrase)
        }
    }

    override fun getItemCount(): Int {
        return searchedPhrases.size
    }
}