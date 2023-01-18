package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization

import android.content.Context
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.CharArraySet
import org.apache.lucene.analysis.ja.JapaneseAnalyzer
import org.apache.lucene.analysis.ja.JapaneseTokenizer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.apache.lucene.util.Version
import java.io.StringReader
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class JWKTokenizer(
    val context : Context
) {

    class Word(
        val japanese : String,
        val translation : String
    )

    private fun buildAnalyzer() : Analyzer {
        val charArraySet = CharArraySet(Version.LUCENE_36, 0, false)
        val stopTags = HashSet<String>()
        return JapaneseAnalyzer(
            Version.LUCENE_36,
            null,
            JapaneseTokenizer.DEFAULT_MODE,
            charArraySet,
            stopTags
        )
    }

    fun fetchWords(phrase : String) : MutableList<Word> {
        val words = ArrayList<Word>()

        val analyzer = this.buildAnalyzer()

        val tokens = ArrayList<String>()

        val tokenStream = analyzer.tokenStream(null, StringReader(phrase))
        tokenStream.reset()
        while (tokenStream.incrementToken()) {
            tokens.add(tokenStream.getAttribute(CharTermAttribute::class.java).toString())
        }
        tokenStream.close()
        analyzer.close()

        val dictionaryJapaneseToEnglish = DictionaryJapaneseToEnglish(context)
        for (token in tokens) {
            val definitions = dictionaryJapaneseToEnglish.define(token)
            var definitionsText = ""
            val definitionsIterator = definitions.iterator()
            while (definitionsIterator.hasNext()) {
                definitionsText += definitionsIterator.next().definitions.contentToString()
                if (definitionsIterator.hasNext()) {
                    definitionsText += "\n"
                }
            }
            val word = Word(
                    japanese = token,
                    translation = definitionsText
            )
            words.add(word)
        }

        return words
    }

}