package com.alfoirazabal.japanesewordkeeper.gui.adapters

import android.content.Context
import android.speech.tts.TextToSpeech
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.definitioncopyer.*
import com.alfoirazabal.japanesewordkeeper.logic.romaji.HiraKataRomajinizer
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition
import com.google.android.material.snackbar.Snackbar
import java.util.*
import kotlin.collections.ArrayList

class DefinitionAdapter : RecyclerView.Adapter<DefinitionAdapter.ViewHolder>() {

    private val definitions = ArrayList<Definition>()

    private val romajinizer = HiraKataRomajinizer()

    private fun copyDefinition(
        definition: Definition, definitionCopyer: IDefinitionCopyer, context: Context) {
        definitionCopyer.copyToClipboard(context, definition)
    }

    private fun hearWord(definition: Definition, context: Context, view : View) {
        lateinit var tts : TextToSpeech
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.JAPANESE
                tts.speak(definition.word, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Snackbar.make(
                    view,
                    R.string.error_message_tts_unavailable,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val txtDefinition : TextView
        val txtDefinitionPhonetic : TextView

        val definitionsMeaningsAdapter = DefinitionMeaningsAdapter()

        init {
            this.txtDefinition = view.findViewById(R.id.txt_definition)
            this.txtDefinitionPhonetic = view.findViewById(R.id.txt_definition_phonetic)
            val recyclerviewMeanings = view.findViewById<RecyclerView>(R.id.recyclerview_meanings)

            recyclerviewMeanings.layoutManager = LinearLayoutManager(view.context)
            recyclerviewMeanings.adapter = definitionsMeaningsAdapter

            view.setOnClickListener {
                val context = view.context
                val definition = definitions[adapterPosition]
                val popupMenu = PopupMenu(view.context, txtDefinitionPhonetic)
                val copySubMenu = popupMenu.menu.addSubMenu(R.string.copy)
                val copyWord = copySubMenu.add(R.string.word)
                val copyPronunciation = copySubMenu.add(R.string.pronunciation)
                val copyRomaji = copySubMenu.add(R.string.romaji)
                val copyDefinition = copySubMenu.add(R.string.definition)
                val copyDefinitionAndMeanings = copySubMenu.add(R.string.definition_and_meanings)
                val listenMenuItem = popupMenu.menu.add(R.string.listen)
                val saveMenuItem = popupMenu.menu.add(R.string.save)
                copyWord.setOnMenuItemClickListener {
                    copyDefinition(definition, DefinitionWordCopyer(), context)
                    true
                }
                copyPronunciation.setOnMenuItemClickListener {
                    copyDefinition(definition, DefinitionPronunciationCopyer(), context)
                    true
                }
                copyRomaji.setOnMenuItemClickListener {
                    copyDefinition(definition, DefinitionRomajiCopyer(), context)
                    true
                }
                copyDefinition.setOnMenuItemClickListener {
                    copyDefinition(definition, DefinitionCopyer(), context)
                    true
                }
                copyDefinitionAndMeanings.setOnMenuItemClickListener {
                    copyDefinition(definition, DefinitionAndMeaningsCopyer(), context)
                    true
                }
                listenMenuItem.setOnMenuItemClickListener {
                    hearWord(definition, context, view)
                    true
                }
                saveMenuItem.setOnMenuItemClickListener {
                    Snackbar.make(
                        view,
                        R.string.msg_will_be_implemented_in_upcoming_releases,
                        Snackbar.LENGTH_LONG
                    ).show()
                    true
                }
                popupMenu.show()
            }
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
    }

    override fun getItemCount(): Int {
        return definitions.size
    }

}