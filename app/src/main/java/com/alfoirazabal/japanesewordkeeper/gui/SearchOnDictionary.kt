package com.alfoirazabal.japanesewordkeeper.gui

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.DictionarySearcherJapaneseEnglish
import java.util.concurrent.atomic.AtomicBoolean

class SearchOnDictionary : AppCompatActivity() {

    private lateinit var etxtSearch : EditText
    private lateinit var imgbtnSearch : ImageView
    private lateinit var layoutLodingDictionary : LinearLayout
    private lateinit var pbrDictionaryWordsSearch : ProgressBar
    private lateinit var recyclerviewFoundWords : RecyclerView

    private val stopSearch = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        etxtSearch = findViewById(R.id.etxt_search)
        imgbtnSearch = findViewById(R.id.imgbtn_search)
        layoutLodingDictionary = findViewById(R.id.layout_loading_dictionary)
        pbrDictionaryWordsSearch = findViewById(R.id.pbr_dictionary_words_search)
        recyclerviewFoundWords = findViewById(R.id.recyclerview_found_words)

        imgbtnSearch.setOnClickListener {
            pbrDictionaryWordsSearch.progress = 0
            pbrDictionaryWordsSearch.visibility = View.VISIBLE
            imgbtnSearch.isEnabled = false
            val searchText = etxtSearch.text.toString()
            val dictionarySearcher = DictionarySearcherJapaneseEnglish(applicationContext)
            dictionarySearcher.onLoadingDictionary = {
                runOnUiThread { layoutLodingDictionary.visibility = View.VISIBLE }
            }
            dictionarySearcher.onLoadedDictionary = {
                runOnUiThread { layoutLodingDictionary.visibility = View.GONE }
            }
            dictionarySearcher.onSearchProgress = { definitionsScanned, definitionsTotal ->
                if (definitionsScanned % 5000 == 0) {
                    val progress = ((definitionsScanned / definitionsTotal.toFloat()) * 100).toInt()
                    runOnUiThread {
                        pbrDictionaryWordsSearch.progress = progress
                    }
                }
            }
            dictionarySearcher.onResultFound = { definition ->
                println(definition.definitions.contentToString())
                // TODO add to recyclerview
            }
            dictionarySearcher.onFinished = {
                runOnUiThread {
                    imgbtnSearch.isEnabled = true
                    pbrDictionaryWordsSearch.progress = 0
                    pbrDictionaryWordsSearch.visibility = View.GONE
                }
            }
            Thread {
                dictionarySearcher.search(searchText, stopSearch)
            }.start()
        }
    }

    override fun onResume() {
        super.onResume()
        this.stopSearch.set(false)
    }

    override fun onPause() {
        this.stopSearch.set(true)
        pbrDictionaryWordsSearch.progress = 0
        super.onPause()
    }

    override fun onStop() {
        this.stopSearch.set(true)
        pbrDictionaryWordsSearch.progress = 0
        super.onStop()
    }

}