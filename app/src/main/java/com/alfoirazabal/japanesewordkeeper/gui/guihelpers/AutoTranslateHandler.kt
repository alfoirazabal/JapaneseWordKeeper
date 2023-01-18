package com.alfoirazabal.japanesewordkeeper.gui.guihelpers

import android.app.Activity
import android.content.Intent
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.alfoirazabal.japanesewordkeeper.gui.DownloadTranslationModels
import com.google.mlkit.common.MlKitException
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions

class AutoTranslateHandler(
    val activity : Activity,
    val spinnerLanguage : Spinner,
    val etxtText : EditText,
    val etxtTranslation : EditText
    ) {

    fun handle() {
        val translatorOptions : TranslatorOptions
        val languageItemPosition = spinnerLanguage.selectedItemPosition
        if (languageItemPosition == 0) {
            translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.ENGLISH)
                .setTargetLanguage(TranslateLanguage.JAPANESE)
                .build()
        } else if (languageItemPosition == 1) {
            translatorOptions = TranslatorOptions.Builder()
                .setSourceLanguage(TranslateLanguage.SPANISH)
                .setTargetLanguage(TranslateLanguage.JAPANESE)
                .build()
        } else {
            throw Error("PROGRAMMATIC ERROR: No language for spinner " +
                    "position: $languageItemPosition")
        }
        val translator = Translation.getClient(translatorOptions)
        translator.translate(etxtText.text.toString())
            .addOnSuccessListener { translationResult ->
                etxtTranslation.setText(translationResult)
            }
            .addOnFailureListener { exception ->
                if (exception is MlKitException) {
                    if (exception.errorCode == MlKitException.NOT_FOUND) {
                        val intentHandleDownloadModels =
                            Intent(this.activity, DownloadTranslationModels::class.java)
                        this.activity.startActivity(intentHandleDownloadModels)
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