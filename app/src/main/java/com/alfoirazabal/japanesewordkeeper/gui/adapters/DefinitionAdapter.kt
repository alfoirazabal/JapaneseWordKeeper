package com.alfoirazabal.japanesewordkeeper.gui.adapters

import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.romaji.HiraKataRomajinizer
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class DefinitionAdapter : RecyclerView.Adapter<DefinitionAdapter.ViewHolder>() {

    private val definitions = ArrayList<Definition>()

    private val romajinizer = HiraKataRomajinizer()

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val imgBtnHearDefinition : ImageView
        val txtDefinition : TextView
        val txtDefinitionPhonetic : TextView

        val definitionsMeaningsAdapter = DefinitionMeaningsAdapter()

        init {
            this.imgBtnHearDefinition = view.findViewById(R.id.imgbtn_hear_definition)
            this.txtDefinition = view.findViewById(R.id.txt_definition)
            this.txtDefinitionPhonetic = view.findViewById(R.id.txt_definition_phonetic)
            val recyclerviewMeanings = view.findViewById<RecyclerView>(R.id.recyclerview_meanings)

            recyclerviewMeanings.layoutManager = LinearLayoutManager(view.context)
            recyclerviewMeanings.adapter = definitionsMeaningsAdapter
        }

    }

    fun setDefinitions(definitions : List<Definition>) {
        this.definitions.clear()
        this.definitions.addAll(definitions)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_dictionary_definition, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val definition = this.definitions[position]
        val phonetics = if (definition.pronunciation != "") {
            definition.pronunciation
        } else {
            definition.word
        }
        val phoneticRomaji = this.romajinizer.romajinize(phonetics)

        holder.txtDefinition.text = definition.word
        holder.txtDefinitionPhonetic.text = "$phonetics = $phoneticRomaji"
        holder.definitionsMeaningsAdapter.setDefinition(definition)
        holder.imgBtnHearDefinition.setOnClickListener {
            lateinit var tts : TextToSpeech
            tts = TextToSpeech(holder.itemView.context) { status ->
                if (status != TextToSpeech.ERROR) {
                    tts.language = Locale.JAPANESE
                    tts.speak(definition.word, TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    Snackbar.make(
                        holder.imgBtnHearDefinition,
                        R.string.error_message_tts_unavailable,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return definitions.size
    }

}