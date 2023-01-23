package com.alfoirazabal.japanesewordkeeper.gui.guihelpers

import android.app.Activity
import android.view.View
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.alfoirazabal.japanesewordkeeper.R
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.mlkit.common.MlKitException
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.TranslateRemoteModel
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class AutoTranslateHandler(val activity : Activity) {

    private val txtTextLanguageMessage : TextView =
        this.activity.findViewById(R.id.txt_text_language_message)
    private val spinnerLanguage : Spinner =
        this.activity.findViewById(R.id.spinner_language)
    private val etxtText : EditText =
        this.activity.findViewById(R.id.etxt_text)
    private val etxtTranslation : EditText =
        this.activity.findViewById(R.id.etxt_translation)

    private fun getTranslatorSourceLanguage() : String {
        val languageItemPosition = spinnerLanguage.selectedItemPosition
        val sourceLanguage : String = when (languageItemPosition) {
            0 -> {
                TranslateLanguage.ENGLISH
            }
            1 -> {
                TranslateLanguage.SPANISH
            }
            else -> {
                throw Error("PROGRAMMATIC ERROR: No language for spinner " +
                        "position: $languageItemPosition")
            }
        }
        return sourceLanguage
    }

    fun isTranslationOptionAvailable() : Task<Boolean> {
        val translateLanguage = this.getTranslatorSourceLanguage()
        val modelManager = RemoteModelManager.getInstance()
        val translationModel = TranslateRemoteModel.Builder(translateLanguage).build()
        return modelManager.isModelDownloaded(translationModel)
    }

    fun startHandleDownloadModels() {
        this.txtTextLanguageMessage.visibility = View.VISIBLE
        this.txtTextLanguageMessage.text = this.activity.getString(
            R.string.msg_downloading_languages_check_notifications
        )
        val optionsEnglishToJapanese = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(TranslateLanguage.JAPANESE)
            .build()
        val optionsSpanishToJapanese = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.SPANISH)
            .setTargetLanguage(TranslateLanguage.JAPANESE)
            .build()
        val optionsJapaneseToEnglish = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.JAPANESE)
            .setTargetLanguage(TranslateLanguage.ENGLISH)
            .build()
        val optionsJapaneseToSpanish = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.JAPANESE)
            .setTargetLanguage(TranslateLanguage.SPANISH)
            .build()
        val englishJapaneseTranslator = Translation.getClient(optionsEnglishToJapanese)
        val spanishJapaneseTranslator = Translation.getClient(optionsSpanishToJapanese)
        val japaneseToEnglishTranslator = Translation.getClient(optionsJapaneseToEnglish)
        val japaneseToSpanishTranslator = Translation.getClient(optionsJapaneseToSpanish)
        val onDownloadError = { exception : Exception ->
            throw Error(exception)
        }
        englishJapaneseTranslator.downloadModelIfNeeded().addOnSuccessListener {
            spanishJapaneseTranslator.downloadModelIfNeeded().addOnSuccessListener {
                japaneseToEnglishTranslator.downloadModelIfNeeded().addOnSuccessListener {
                    japaneseToSpanishTranslator.downloadModelIfNeeded().addOnSuccessListener {
                        this.txtTextLanguageMessage.visibility = View.GONE
                        Snackbar.make(
                            this.etxtText,
                            R.string.msg_translation_languages_download_finished,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }.addOnFailureListener(onDownloadError)
                }.addOnFailureListener(onDownloadError)
            }.addOnFailureListener(onDownloadError)
        }.addOnFailureListener(onDownloadError)
    }

    fun handle() {
        val sourceLanguage = this.getTranslatorSourceLanguage()
        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage)
            .setTargetLanguage(TranslateLanguage.JAPANESE)
            .build()
        val translator = Translation.getClient(translatorOptions)
        translator.translate(etxtText.text.toString())
            .addOnSuccessListener { translationResult ->
                etxtTranslation.setText(translationResult)
            }
            .addOnFailureListener { exception ->
                if (exception is MlKitException) {
                    if (exception.errorCode == MlKitException.NOT_FOUND) {
                        this.startHandleDownloadModels()
                    } else {
                        Toast
                            .makeText(this.activity, exception.message, Toast.LENGTH_LONG)
                            .show()
                    }
                } else {
                    Toast
                        .makeText(this.activity, exception.message, Toast.LENGTH_LONG)
                        .show()
                }

            }
    }

}