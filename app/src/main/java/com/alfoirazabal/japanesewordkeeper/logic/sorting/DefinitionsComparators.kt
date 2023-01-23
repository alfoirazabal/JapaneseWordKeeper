package com.alfoirazabal.japanesewordkeeper.logic.sorting

import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.Definition
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.meaning.MeaningParser

class DefinitionsComparators {

    private val meaningsParser : MeaningParser = MeaningParser()

    fun getSearchTermComparator(searchTerm : String): Comparator<Definition> {
        return Comparator { o1, o2 ->
            val wordComparisonO1 = o1.word.compareTo(searchTerm)
            val wordComparisonO2 = o2.word.compareTo(searchTerm)
            val pronunciationComparisonO1 = o1.pronunciation.compareTo(searchTerm)
            val pronunciationComparisonO2 = o2.pronunciation.compareTo(searchTerm)
            var minimumMeaningComparisonO1 = Integer.MAX_VALUE
            for (definitionO1 in o1.definitions) {
                val parsedDefinitionO1 = meaningsParser.parse(definitionO1)
                val currentMeaningComparison = parsedDefinitionO1.meaning.compareTo(searchTerm)
                if (currentMeaningComparison < minimumMeaningComparisonO1) {
                    minimumMeaningComparisonO1 = currentMeaningComparison
                }
            }
            var minimumMeaningComparisonO2 = Integer.MAX_VALUE
            for (definitionO2 in o2.definitions) {
                val parsedDefinitionO2 = meaningsParser.parse(definitionO2)
                val currentMeaningComparison = parsedDefinitionO2.meaning.compareTo(searchTerm)
                if (currentMeaningComparison < minimumMeaningComparisonO2) {
                    minimumMeaningComparisonO2 = currentMeaningComparison
                }
            }
            val minimumO1 = arrayOf(
                wordComparisonO1, pronunciationComparisonO1, minimumMeaningComparisonO1
            ).min()
            val minimumO2 = arrayOf(
                wordComparisonO2, pronunciationComparisonO2, minimumMeaningComparisonO2
            ).min()
            minimumO1.compareTo(minimumO2)
        }
    }

}