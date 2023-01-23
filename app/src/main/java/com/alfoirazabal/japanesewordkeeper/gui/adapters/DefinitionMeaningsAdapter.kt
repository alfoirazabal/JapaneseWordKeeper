package com.alfoirazabal.japanesewordkeeper.gui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.meaning.MeaningParser
import kotlin.collections.ArrayList

class DefinitionMeaningsAdapter : RecyclerView.Adapter<DefinitionMeaningsAdapter.ViewHolder>() {

    private val definitions = ArrayList<String>()

    private val meaningParser = MeaningParser()

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        val txtMeaningDescriptors : TextView
        val txtMeaning : TextView

        init {
            txtMeaningDescriptors = view.findViewById(R.id.txt_meaning_descriptors)
            txtMeaning = view.findViewById(R.id.txt_meaning)
        }

    }

    fun setDefinition(definition : Definition) {
        this.definitions.clear()
        this.notifyItemRangeRemoved(0, this.itemCount)
        this.definitions.addAll(definition.definitions)
        this.notifyItemRangeInserted(0, this.itemCount)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.recyclerview_dictionary_definition_meaning, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val definitionMeaning = meaningParser.parse(definitions[position])
        val descriptorsText = definitionMeaning.descriptors.map { it.descriptor.toList() }
            .flatten().toTypedArray()
        if (descriptorsText.isNotEmpty()) {
            holder.txtMeaningDescriptors.text = descriptorsText.contentToString()
            holder.txtMeaningDescriptors.visibility = View.VISIBLE
        } else {
            holder.txtMeaningDescriptors.visibility = View.GONE
        }
        holder.txtMeaning.text = definitionMeaning.meaning
    }

    override fun getItemCount(): Int {
        return this.definitions.size
    }

}