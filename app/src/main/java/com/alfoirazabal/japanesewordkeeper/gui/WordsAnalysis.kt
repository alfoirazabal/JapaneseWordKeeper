package com.alfoirazabal.japanesewordkeeper.gui

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.Database
import com.alfoirazabal.japanesewordkeeper.gui.adapters.WordsAnalysisAdapter
import com.alfoirazabal.japanesewordkeeper.gui.constants.BundleConstants
import com.alfoirazabal.japanesewordkeeper.gui.guihelpers.PhraseLanguageCommons

class WordsAnalysis : AppCompatActivity() {

    private lateinit var txtLanguage : TextView
    private lateinit var txtText : TextView
    private lateinit var layoutRomaji : LinearLayout
    private lateinit var txtRomaji : TextView
    private lateinit var txtTranslation : TextView

    private lateinit var wordsAnalysisAdapter: WordsAnalysisAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_words_analysis)

        txtLanguage = findViewById(R.id.txt_phrase_language)
        txtText = findViewById(R.id.txt_text)
        layoutRomaji = findViewById(R.id.layout_romaji)
        txtRomaji = findViewById(R.id.txt_romaji)
        txtTranslation = findViewById(R.id.txt_translation)
        val recyclerViewWordsAnalysis = findViewById<RecyclerView>(R.id.recyclerview_words_analysis)
        recyclerViewWordsAnalysis.layoutManager = LinearLayoutManager(applicationContext)

        wordsAnalysisAdapter = WordsAnalysisAdapter()
        recyclerViewWordsAnalysis.adapter = wordsAnalysisAdapter
    }

    override fun onResume() {
        super.onResume()
        val phraseId = intent.getStringExtra(BundleConstants.phraseId)!!
        val database = Database.get(applicationContext)
        Thread {
            val phrase = database.phrasesDAO().getById(phraseId)
            wordsAnalysisAdapter.phrase = phrase
            wordsAnalysisAdapter.processPhrase(applicationContext)
            runOnUiThread {
                txtLanguage.text = PhraseLanguageCommons(applicationContext, phrase).getFlagEmoji()
                txtText.text = phrase.text
                if (phrase.romaji != "") {
                    txtRomaji.text = phrase.romaji
                    layoutRomaji.visibility = View.VISIBLE
                } else {
                    layoutRomaji.visibility = View.GONE
                }
                txtTranslation.text = phrase.translation
                wordsAnalysisAdapter.notifyDataSetChanged()
            }
        }.start()
    }

}