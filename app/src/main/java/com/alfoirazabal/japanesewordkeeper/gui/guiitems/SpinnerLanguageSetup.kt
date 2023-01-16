package com.alfoirazabal.japanesewordkeeper.gui.guiitems

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.alfoirazabal.japanesewordkeeper.R

class SpinnerLanguageSetup {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var spinner: Spinner

    fun setup(context: Context) {
        val lastTextLanguageIndexKey = context.getString(R.string.preferences_last_text_language_index)

        val lastLanguageIndex = sharedPreferences.getInt(lastTextLanguageIndexKey, 0)
        spinner.setSelection(lastLanguageIndex)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val sharedPreferencesEditor = sharedPreferences.edit()

                sharedPreferencesEditor.putInt(lastTextLanguageIndexKey, position)
                sharedPreferencesEditor.apply()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }
    }

    fun setSelectedLanguage(language : String) {
        when (language) {
            "en" -> {
                spinner.setSelection(0)
            }
            "es" -> {
                spinner.setSelection(1)
            }
            else -> {
                throw Error(
                    "Could not find text language for phrase language: $language"
                )
            }
        }
    }

    fun getSelectedLanguage() : String {
        val language : String = when (spinner.selectedItemPosition) {
            0 -> {
                "en"
            }
            1 -> {
                "es"
            }
            else -> {
                throw Error(
                    "Could not find text language for selected spinner language"
                )
            }
        }
        return language
    }

}