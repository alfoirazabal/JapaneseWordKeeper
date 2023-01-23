package com.alfoirazabal.japanesewordkeeper.gui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.romaji.HiraKataRomajinizer
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition

class DefinitionAdapter : RecyclerView.Adapter<DefinitionAdapter.ViewHolder>() {

    private val definitions = ArrayList<Definition>()

    private val romajinizer = HiraKataRomajinizer()

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val txtDefinition : TextView
        val txtDefinitionPhonetic : TextView

        init {
            this.txtDefinition = view.findViewById(R.id.txt_definition)
            this.txtDefinitionPhonetic = view.findViewById(R.id.txt_definition_phonetic)
        }

    }

    fun addDefinition(definition : Definition) {
        this.definitions.add(definition)
        this.notifyItemInserted(this.definitions.size)
    }

    fun resetDefinitions() {
        this.definitions.clear()
        this.notifyItemRangeRemoved(0, this.itemCount)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_dictionary_definition, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val definition = this.definitions[position]
        val phonetics : String
        if (definition.pronunciation != "") {
            phonetics = definition.pronunciation
        } else {
            phonetics = definition.word
        }
        val phoneticRomaji = this.romajinizer.romajinize(phonetics)
        holder.txtDefinition.text = definition.word
        holder.txtDefinitionPhonetic.text = "$phonetics = $phoneticRomaji"
    }

    override fun getItemCount(): Int {
        return definitions.size
    }

}