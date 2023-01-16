package com.alfoirazabal.japanesewordkeeper.gui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.gui.helpers.PhrasesSorter

class AppSettings : AppCompatActivity() {

    private lateinit var phrasesSorter: PhrasesSorter

    private lateinit var sortSpinner : Spinner
    private lateinit var rbtngrpSorting : RadioGroup
    private lateinit var sortRadioButtonAscending : RadioButton
    private lateinit var sortRadioButtonDescending : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        phrasesSorter = PhrasesSorter(applicationContext)

        sortSpinner = findViewById(R.id.spinner_sorting_type)
        rbtngrpSorting = findViewById(R.id.rbtngrp_sorting)
        sortRadioButtonAscending = findViewById(R.id.rbtn_sorting_ascending)
        sortRadioButtonDescending = findViewById(R.id.rbtn_sorting_descending)

        val sortSpinnerAdapter = ArrayAdapter(
            applicationContext, R.layout.spinner_dropdown_sorting_type, phrasesSorter.sortableTypes
        )
        sortSpinner.adapter = sortSpinnerAdapter

        val currentSortableType = phrasesSorter.getType()
        val currentSortableOrder = phrasesSorter.getOrder()

        val sortableTypePosition = sortSpinnerAdapter.getPosition(currentSortableType)
        sortSpinner.setSelection(sortableTypePosition)

        when (currentSortableOrder) {
            applicationContext.getString(R.string.sorting_order_ascending) -> {
                sortRadioButtonAscending.isChecked = true
            }
            applicationContext.getString(R.string.sorting_order_descending) -> {
                sortRadioButtonDescending.isChecked = true
            }
        }

        sortSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val sortType = sortSpinner.selectedItem.toString()
                phrasesSorter.setType(sortType)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }

        rbtngrpSorting.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbtn_sorting_ascending -> {
                    phrasesSorter.setOrder(getString(R.string.sorting_order_ascending))
                }
                R.id.rbtn_sorting_descending -> {
                    phrasesSorter.setOrder(getString(R.string.sorting_order_descending))
                }
            }
        }

    }

}