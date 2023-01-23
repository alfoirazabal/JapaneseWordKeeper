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
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase
import com.alfoirazabal.japanesewordkeeper.gui.adapters.PhraseAdapterCharacters
import com.alfoirazabal.japanesewordkeeper.gui.constants.BundleConstants
import com.alfoirazabal.japanesewordkeeper.gui.guihelpers.PhraseLanguageCommons

class SymbolsOverview : AppCompatActivity() {

    private lateinit var txtPhraseLanguage : TextView
    private lateinit var txtText : TextView
    private lateinit var layoutRomaji : LinearLayout
    private lateinit var txtRomaji : TextView
    private lateinit var txtTranslation : TextView

    private lateinit var adapterSymbolsOverview : PhraseAdapterCharacters

    private var phrase : Phrase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symbols_overview)

        txtPhraseLanguage = findViewById(R.id.txt_phrase_language)
        txtText = findViewById(R.id.txt_text)
        layoutRomaji = findViewById(R.id.layout_romaji)
        txtRomaji = findViewById(R.id.txt_romaji)
        txtTranslation = findViewById(R.id.txt_translation)
        val recyclerViewSymbolsOverview =
            findViewById<RecyclerView>(R.id.recyclerview_symbols_overview)

        adapterSymbolsOverview = PhraseAdapterCharacters()

        recyclerViewSymbolsOverview.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewSymbolsOverview.adapter = adapterSymbolsOverview

    }

    override fun onResume() {
        super.onResume()
        val phraseId = intent.getStringExtra(BundleConstants.phraseId)!!
        val db = Database.get(applicationContext)
        Thread {
            phrase = db.phrasesDAO().getById(phraseId)
            val phraseLanguageCommons = PhraseLanguageCommons(
                context = applicationContext, phrase = phrase!!
            )
            runOnUiThread {
                txtPhraseLanguage.text = phraseLanguageCommons.getFlagEmoji()
                txtText.text = phrase!!.text
                txtRomaji.text = phrase!!.romaji
                if (phrase!!.romaji == "") layoutRomaji.visibility = View.GONE
                else layoutRomaji.visibility = View.VISIBLE
                txtTranslation.text = phrase!!.translation
                adapterSymbolsOverview.setPhrase(phrase!!.translation, applicationContext)
                adapterSymbolsOverview.notifyDataSetChanged()
            }
        }.start()
    }

}