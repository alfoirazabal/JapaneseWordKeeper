package com.alfoirazabal.japanesewordkeeper.gui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.alfoirazabal.japanesewordkeeper.R
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class DownloadTranslationModels : AppCompatActivity() {

    private lateinit var txtDownloadingEnglishToJapanese : TextView
    private lateinit var pbrDownloadingEnglishToJapanese : ProgressBar
    private lateinit var txtDownloadingSpanishToJapanese : TextView
    private lateinit var pbrDownloadingSpanishToJapanese : ProgressBar
    private lateinit var btnOk : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download_translation_models)

        txtDownloadingEnglishToJapanese = findViewById(R.id.txt_downloading_english_to_japanese)
        pbrDownloadingEnglishToJapanese = findViewById(R.id.pbr_downloading_english_to_japanese)
        txtDownloadingSpanishToJapanese = findViewById(R.id.txt_downloading_spanish_to_japanese)
        pbrDownloadingSpanishToJapanese = findViewById(R.id.pbr_downloading_spanish_to_japanese)
        btnOk = findViewById(R.id.btn_ok)
    }

    override fun onResume() {
        super.onResume()
        val optionsEnglishToJapanese = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.JAPANESE)
            .build()
        val optionsSpanishToJapanese = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(TranslateLanguage.JAPANESE)
            .build()
        val englishJapaneseTranslator = Translation.getClient(optionsEnglishToJapanese)
        val spanishJapaneseTranslator = Translation.getClient(optionsSpanishToJapanese)
        englishJapaneseTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                txtDownloadingEnglishToJapanese.text = getString(R.string.downloaded)
                pbrDownloadingEnglishToJapanese.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                throw Error(exception)
            }
        spanishJapaneseTranslator.downloadModelIfNeeded()
            .addOnSuccessListener {
                txtDownloadingSpanishToJapanese.text = getString(R.string.downloaded)
                pbrDownloadingSpanishToJapanese.visibility = View.GONE
            }
            .addOnFailureListener { exception ->
                throw Error(exception)
            }

        btnOk.setOnClickListener {
            finish()
        }
    }

}