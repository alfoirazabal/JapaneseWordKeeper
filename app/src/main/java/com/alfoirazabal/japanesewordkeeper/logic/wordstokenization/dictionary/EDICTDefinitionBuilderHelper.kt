package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary

class EDICTDefinitionBuilderHelper {

    fun parseDefinitions(definitionPart: String): Array<String> {
        return definitionPart.split("/").filter { it != "" }.toTypedArray()
    }

    fun parseJapaneseParts(japaneseParts: String): Array<String> {
        val splittedParts = arrayOf("", "")
        val firstParenthesisIndex = japaneseParts.indexOf("[")
        val lastParenthesisIndex = japaneseParts.indexOf("]")
        if (firstParenthesisIndex != -1 && lastParenthesisIndex != -1) {
            val pronunciation =
                japaneseParts.substring(firstParenthesisIndex + 1, lastParenthesisIndex)
            val word = japaneseParts.substring(0, firstParenthesisIndex - 1)
            splittedParts[0] = word
            splittedParts[1] = pronunciation
        } else {
            splittedParts[0] = japaneseParts
        }
        return splittedParts
    }

}