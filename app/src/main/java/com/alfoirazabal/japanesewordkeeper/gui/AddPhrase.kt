package com.alfoirazabal.japanesewordkeeper.gui

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.Database
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase
import com.alfoirazabal.japanesewordkeeper.gui.guiitems.SpinnerLanguageSetup

class AddPhrase : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_phrase)

        val spinnerLanguage = findViewById<Spinner>(R.id.spinner_language)
        val etxtText = findViewById<EditText>(R.id.etxt_text)
        val etxtRomaji = findViewById<EditText>(R.id.etxt_romaji)
        val etxtTranslation = findViewById<EditText>(R.id.etxt_translation)
        val btnAdd = findViewById<Button>(R.id.btn_add)

        btnAdd.setOnClickListener {
            btnAdd.isEnabled = false
            val phrase = Phrase()
            phrase.text = etxtText.text.toString()
            phrase.romaji = etxtRomaji.text.toString()
            phrase.translation = etxtTranslation.text.toString()
            val languageItemPosition = spinnerLanguage.selectedItemPosition
            if (languageItemPosition == 0) {
                phrase.textLanguage = "en"
            } else if (languageItemPosition == 1) {
                phrase.textLanguage = "es"
            }
            Thread {
                val db = Database.get(applicationContext)
                db.phrasesDAO().insert(phrase)
                runOnUiThread {
                    finish()
                }
            }.start()
        }

        val sharedPreferences = applicationContext.getSharedPreferences(
            getString(R.string.preference_file_name), Context.MODE_PRIVATE
        )
        val spinnerLanguageSetup = SpinnerLanguageSetup()
        spinnerLanguageSetup.spinner = spinnerLanguage
        spinnerLanguageSetup.sharedPreferences = sharedPreferences
        spinnerLanguageSetup.setup(applicationContext)
    }

}