package com.alfoirazabal.japanesewordkeeper.gui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.Database
import com.alfoirazabal.japanesewordkeeper.gui.constants.BundleConstants
import com.alfoirazabal.japanesewordkeeper.gui.helpers.PhrasesSorter

class MainActivity : AppCompatActivity() {

    private val phrasesAdapter = PhraseAdapter()

    private lateinit var phrasesSorter: PhrasesSorter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        phrasesSorter = PhrasesSorter(applicationContext)

        val recyclerviewLayoutManager = LinearLayoutManager(applicationContext)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview_phrases)
        recyclerView.layoutManager = recyclerviewLayoutManager
        recyclerView.adapter = phrasesAdapter

        val btnAddPhrase = findViewById<Button>(R.id.btn_add)
        val etxtSearch = findViewById<EditText>(R.id.etxt_search)

        btnAddPhrase.setOnClickListener {
            val addPhraseActivity = Intent(applicationContext, AddPhrase::class.java)
            startActivity(addPhraseActivity)
        }
        etxtSearch.addTextChangedListener {
            val searchQuery = it.toString()
            phrasesAdapter.updateSearchQuery(searchQuery)
        }

        phrasesAdapter.onPhraseClicked = { phrase ->
            val phraseId = phrase.id
            val intentViewPhrase = Intent(applicationContext, ViewPhrase::class.java)
            intentViewPhrase.putExtra(BundleConstants.phraseId, phraseId)
            startActivity(intentViewPhrase)
        }

    }

    override fun onResume() {
        super.onResume()
        Thread {
            val db = Database.get(applicationContext)
            val allPhrases = db.phrasesDAO().getAll()
            phrasesSorter.sort(allPhrases)
            phrasesAdapter.setPhrases(allPhrases)
            runOnUiThread { phrasesAdapter.notifyDataSetChanged() }
        }.start()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_main_menu, menu)

        val settingsMenuItem = menu.findItem(R.id.menu_item_settings)
        settingsMenuItem.setOnMenuItemClickListener {
            val settingsIntent = Intent(applicationContext, AppSettings::class.java)
            startActivity(settingsIntent)
            true
        }

        return super.onCreateOptionsMenu(menu)
    }

}