package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.meaning

import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.meaning.DefinitionMeaning
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.meaning.DefinitionMeaningDescriptor

class MeaningParser {

    fun parse(rawMeaning : String) : DefinitionMeaning {
        var parsingDescriptor = false
        var endDescriptorParser = false
        var descriptorStringBuilder = StringBuilder()
        val definitionMeaningDescriptors = ArrayList<DefinitionMeaningDescriptor>()
        val definitionStringBuilder = StringBuilder()
        var x = 0
        while (x < rawMeaning.length) {
            val rawMeaningCharacter = rawMeaning[x]
            if (!endDescriptorParser) {
                if (rawMeaningCharacter == ')') {
                    val descriptorText = descriptorStringBuilder.toString()
                    val meaningDescriptor = DefinitionMeaningDescriptor(
                        descriptor = descriptorText.split(",").toTypedArray()
                    )
                    definitionMeaningDescriptors.add(meaningDescriptor)
                    descriptorStringBuilder = StringBuilder()
                    if (x + 2 < rawMeaning.length && rawMeaning[x + 2] != '(') {
                        endDescriptorParser = true
                    }
                    x += 2
                } else if (rawMeaningCharacter == '(') {
                    parsingDescriptor = true
                    x++
                } else if (parsingDescriptor) {
                    descriptorStringBuilder.append(rawMeaningCharacter)
                    x++
                } else {
                    endDescriptorParser = true
                    definitionStringBuilder.append(rawMeaningCharacter)
                    x++
                }
            } else {
                definitionStringBuilder.append(rawMeaningCharacter)
                x++
            }
        }
        return DefinitionMeaning(
            descriptors = definitionMeaningDescriptors.toTypedArray(),
            meaning = definitionStringBuilder.toString()
        )
    }

}