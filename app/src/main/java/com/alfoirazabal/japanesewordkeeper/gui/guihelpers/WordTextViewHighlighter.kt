package com.alfoirazabal.japanesewordkeeper.gui.guihelpers

import android.text.Html
import android.widget.TextView

class WordTextViewHighlighter(
    private val textView: TextView
) {

    fun stylize(text : String, highlightedWords : List<String>) {
        val stringBuilder = StringBuilder()
        val highlightedWordRanges = ArrayList<IntRange>()
        for (highlightedWord in highlightedWords) {
            var currentStartIndex = text.indexOf(highlightedWord)
            while (currentStartIndex >= 0) {
                val endIndex = currentStartIndex + highlightedWord.length
                highlightedWordRanges.add(IntRange(currentStartIndex, endIndex))
                currentStartIndex = text.indexOf(highlightedWord, currentStartIndex + 1)
            }
        }
        highlightedWordRanges.sortBy { it.first }
        var textCharIndex = 0
        var isInHighlightedTag = false
        while (textCharIndex < text.length) {
            val char = text[textCharIndex]
            val charIsHighlighted = highlightedWordRanges.any {
                textCharIndex >= it.first && textCharIndex < it.last
            }
            if (charIsHighlighted) {
                if (!isInHighlightedTag) {
                    stringBuilder.append("<font color='red'>")
                    isInHighlightedTag = true
                }
                stringBuilder.append(char)
            } else {
                if (isInHighlightedTag) {
                    stringBuilder.append("</font>")
                    isInHighlightedTag = false
                }
                stringBuilder.append(char)
            }
            textCharIndex++
        }
        val htmlText = stringBuilder.toString()
        textView.setText(Html.fromHtml(htmlText), TextView.BufferType.SPANNABLE)
    }

}