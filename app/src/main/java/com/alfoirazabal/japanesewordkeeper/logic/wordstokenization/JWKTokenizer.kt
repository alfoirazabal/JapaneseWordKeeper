package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization

import com.google.mlkit.common.MlKitException
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.ja.JapaneseAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.util.Version
import java.io.StringReader

class JWKTokenizer {

    class Word(
        val japanese : String,
        val translation : String
    )

    private fun buildAnalyzer() : Analyzer {
        return JapaneseAnalyzer(Version.LUCENE_36)
    }

    var onWordFetched : (words : Word) -> Unit = { }
    var onFinished : () -> Unit = { }
    var onTranslationUnavailable : () -> Unit = { }

    fun fetchWords(phrase : String, translationLanguage : String) {
        val analyzer = this.buildAnalyzer()

        val tokens = ArrayList<String>()

        val tokenStream = analyzer.tokenStream(null, StringReader(phrase))
        tokenStream.reset()
        while (tokenStream.incrementToken()) {
            tokens.add(tokenStream.getAttribute(CharTermAttribute::class.java).toString())
        }
        tokenStream.close()
        analyzer.close()

        val translatorOptions : TranslatorOptions
        when (translationLanguage) {
            "en" -> {
                translatorOptions = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.JAPANESE)
                    .setTargetLanguage(TranslateLanguage.ENGLISH)
                    .build()
            }
            "es" -> {
                translatorOptions = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.JAPANESE)
                    .setTargetLanguage(TranslateLanguage.SPANISH)
                    .build()
            }
            else -> {
                throw Error("No known translation language: " + translationLanguage)
            }
        }
        val translator = Translation.getClient(translatorOptions)
        for (i in 0 until tokens.size) {
            val token = tokens[i]
            translator.translate(token)
                .addOnSuccessListener { translation ->
                    val word = Word(japanese = token, translation = translation)
                    this.onWordFetched.invoke(word)
                    if (i == tokens.size - 1) {
                        this.onFinished.invoke()
                    }
                }
                .addOnFailureListener { exception ->
                    if (exception is MlKitException) {
                        if (exception.errorCode == MlKitException.NOT_FOUND) {
                            this.onTranslationUnavailable.invoke()
                        } else {
                            throw exception
                        }
                    } else {
                        throw exception
                    }
                }
        }

    }

}