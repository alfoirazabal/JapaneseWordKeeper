package com.alfoirazabal.japanesewordkeeper.gui

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.Database
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase
import com.alfoirazabal.japanesewordkeeper.gui.guihelpers.AutoTranslateHandler
import com.alfoirazabal.japanesewordkeeper.gui.guiitems.SpinnerLanguageSetup

class AddPhrase : AppCompatActivity() {

    private val autoTranslateLooperHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_phrase)

        val spinnerLanguage = findViewById<Spinner>(R.id.spinner_language)
        val etxtText = findViewById<EditText>(R.id.etxt_text)
        val switchAutoTranslate = findViewById<SwitchCompat>(R.id.switch_auto_translate)
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

        val autoTranslateHandler = AutoTranslateHandler(activity = this)

        switchAutoTranslate.setOnCheckedChangeListener { _, _ ->
            autoTranslateHandler.isTranslationOptionAvailable()
                .addOnFailureListener { throw Error(it) }
                .addOnCompleteListener {
                    if (it.result == null || it.result == false) {
                        autoTranslateHandler.startHandleDownloadModels()
                    }
                }
        }

        val autoTranslate = object : kotlinx.coroutines.Runnable {
            override fun run() {
                if (switchAutoTranslate.isChecked) {
                    autoTranslateHandler.handle()
                }
                autoTranslateLooperHandler.postDelayed(this, 750)
            }
        }
        this.autoTranslateLooperHandler.postDelayed(autoTranslate, 750)

        val sharedPreferences = applicationContext.getSharedPreferences(
            getString(R.string.preference_file_name), Context.MODE_PRIVATE
        )
        val spinnerLanguageSetup = SpinnerLanguageSetup()
        spinnerLanguageSetup.spinner = spinnerLanguage
        spinnerLanguageSetup.sharedPreferences = sharedPreferences
        spinnerLanguageSetup.setup(applicationContext)
    }

    override fun onStop() {
        this.autoTranslateLooperHandler.removeCallbacksAndMessages(null)
        super.onStop()
    }

}