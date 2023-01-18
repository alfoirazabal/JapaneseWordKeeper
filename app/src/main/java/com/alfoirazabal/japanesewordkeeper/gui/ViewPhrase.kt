package com.alfoirazabal.japanesewordkeeper.gui

import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.Database
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase
import com.alfoirazabal.japanesewordkeeper.gui.constants.BundleConstants
import com.google.android.material.snackbar.Snackbar
import java.util.*

class ViewPhrase : AppCompatActivity() {

    private lateinit var phraseId : String

    private lateinit var txtTextLanguage : TextView
    private lateinit var txtText : TextView
    private lateinit var txtRomaji : TextView
    private lateinit var txtTranslation : TextView
    private lateinit var imgPlayTranslationSound : ImageView
    private lateinit var txtDateCreated : TextView
    private lateinit var txtDateModified : TextView
    private lateinit var txtDateLastAccessed : TextView
    private lateinit var recyclerViewSymbolsOverview : RecyclerView
    private lateinit var btnEdit : Button
    private lateinit var btnDelete : Button

    private lateinit var tts : TextToSpeech
    private var ttsEnabled : Boolean = false

    private var phrase : Phrase? = null

    private var firstTimeAccessed : Boolean = true

    private lateinit var adapterPhraseCharacters : PhraseAdapterCharacters

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_phrase)

        txtTextLanguage = findViewById(R.id.txt_text_language)
        txtText = findViewById(R.id.txt_text)
        txtRomaji = findViewById(R.id.txt_romaji)
        txtTranslation = findViewById(R.id.txt_translation)
        imgPlayTranslationSound = findViewById(R.id.img_hear_translation)
        txtDateCreated = findViewById(R.id.txt_date_created)
        txtDateModified = findViewById(R.id.txt_date_modified)
        txtDateLastAccessed = findViewById(R.id.txt_date_last_accessed)
        recyclerViewSymbolsOverview = findViewById(R.id.recyclerview_symbols_overview)
        btnEdit = findViewById(R.id.btn_edit)
        btnDelete = findViewById(R.id.btn_delete)

        adapterPhraseCharacters = PhraseAdapterCharacters()
        recyclerViewSymbolsOverview.layoutManager = LinearLayoutManager(applicationContext)
        recyclerViewSymbolsOverview.adapter = adapterPhraseCharacters

        phraseId = intent.getStringExtra(BundleConstants.phraseId)!!

        tts = TextToSpeech(applicationContext) { status ->
            if (status != TextToSpeech.ERROR) {
                ttsEnabled = true
                tts.language = Locale.JAPANESE
            }
        }

        btnEdit.setOnClickListener {
            val intentEdit = Intent(applicationContext, EditPhrase::class.java)
            intentEdit.putExtra(BundleConstants.phraseId, phraseId)
            startActivity(intentEdit)
        }

        btnDelete.setOnClickListener {
            if (phrase != null) {
                AlertDialog.Builder(this)
                    .setTitle(R.string.msg_delete_phrase_title)
                    .setMessage(R.string.msg_delete_phrase_content)
                    .setIcon(android.R.drawable.ic_delete)
                    .setPositiveButton(R.string.yes) { _, _ ->
                        btnDelete.isEnabled = false
                        btnEdit.isEnabled = false
                        Thread {
                            val db = Database.get(applicationContext)
                            db.phrasesDAO().delete(phrase!!)
                            runOnUiThread { finish() }
                        }.start()

                    }.setNegativeButton(R.string.no) { _, _ -> }
                    .show()
            }
        }

        imgPlayTranslationSound.setOnClickListener {
            if (ttsEnabled) {
                tts.speak(phrase?.translation, TextToSpeech.QUEUE_FLUSH, null, null)
            } else {
                Snackbar.make(
                    imgPlayTranslationSound,
                    R.string.error_message_tts_unavailable,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        Thread {
            val db = Database.get(applicationContext)
            phrase = db.phrasesDAO().getById(phraseId)
            if (firstTimeAccessed) {
                phrase!!.dateLastAccessed = Date()
                db.phrasesDAO().update(phrase!!)
                firstTimeAccessed = false
            }
            runOnUiThread {
                when (phrase!!.textLanguage) {
                    "es" -> {
                        txtTextLanguage.text = getString(R.string.text_language_es)
                    }
                    "en" -> {
                        txtTextLanguage.text = getString(R.string.text_language_en)
                    }
                    else -> {
                        throw Error("Text language unknown: " + phrase!!.textLanguage)
                    }
                }
                txtText.text = phrase!!.text
                txtRomaji.text = phrase!!.romaji
                txtTranslation.text = phrase!!.translation
                txtDateCreated.text = phrase!!.dateCreated.toString()
                txtDateModified.text = phrase!!.dateModified.toString()
                txtDateLastAccessed.text = phrase!!.dateLastAccessed.toString()

                adapterPhraseCharacters.setPhrase(phrase!!.translation, applicationContext)
                adapterPhraseCharacters.notifyDataSetChanged()
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        tts.stop()
    }

}