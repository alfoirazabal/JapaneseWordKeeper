package com.alfoirazabal.japanesewordkeeper

import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.meaning.MeaningParser
import com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.meaning.DefinitionMeaningDescriptor
import junit.framework.Assert.assertEquals
import org.junit.Assert.assertArrayEquals
import org.junit.Test

class MeaningParserTests {

    private val meaningParser : MeaningParser = MeaningParser()

    @Test
    fun parseTest1() {
        val meaningPhrase = "guinea"
        val definitionMeaning = meaningParser.parse(meaningPhrase)
        assertEquals("guinea", definitionMeaning.meaning)
        assertEquals(true, definitionMeaning.descriptors.isEmpty())
    }

    @Test
    fun parseTest2() {
        val meaningPhrase = "(n) (uk) Guinea baboon (Papio papio)"
        val definitionMeaning = meaningParser.parse(meaningPhrase)
        val expectedDescriptor1 = DefinitionMeaningDescriptor(arrayOf("n"))
        val expectedDescriptor2 = DefinitionMeaningDescriptor(arrayOf("uk"))
        val expectedMeaningDescriptors = arrayOf(expectedDescriptor1, expectedDescriptor2)
        assertEquals("Guinea baboon (Papio papio)", definitionMeaning.meaning)
        assertArrayEquals(expectedMeaningDescriptors, definitionMeaning.descriptors)
    }

    @Test
    fun parseTest3() {
        val meaningPhrase = "(n,adj-no) gifted (person)"
        val definitionMeaning = meaningParser.parse(meaningPhrase)
        val expectedDescriptor1 = DefinitionMeaningDescriptor(arrayOf("n", "adj-no"))
        val expectedMeaningDescriptors = arrayOf(expectedDescriptor1)
        assertEquals("gifted (person)", definitionMeaning.meaning)
        assertArrayEquals(expectedMeaningDescriptors, definitionMeaning.descriptors)
    }

    @Test
    fun parseTest4() {
        val meaningPhrase = "(n,vs) (1) give"
        val definitionMeaning = meaningParser.parse(meaningPhrase)
        val expectedDescriptor1 = DefinitionMeaningDescriptor(arrayOf("n", "vs"))
        val expectedDescriptor2 = DefinitionMeaningDescriptor(arrayOf("1"))
        val expectedMeaningDescriptors = arrayOf(expectedDescriptor1, expectedDescriptor2)
        assertEquals("give", definitionMeaning.meaning)
        assertArrayEquals(expectedMeaningDescriptors, definitionMeaning.descriptors)
    }

    @Test
    fun parseTest5() {
        val meaningPhrase = "(1) (on-mim) (adv,n) screaming"
        val definitionMeaning = meaningParser.parse(meaningPhrase)
        val expectedDescriptor1 = DefinitionMeaningDescriptor(arrayOf("1"))
        val expectedDescriptor2 = DefinitionMeaningDescriptor(arrayOf("on-mim"))
        val expectedDescriptor3 = DefinitionMeaningDescriptor(arrayOf("adv", "n"))
        val expectedMeaningDescriptors =
            arrayOf(expectedDescriptor1, expectedDescriptor2, expectedDescriptor3)
        assertEquals("screaming", definitionMeaning.meaning)
        assertArrayEquals(expectedMeaningDescriptors, definitionMeaning.descriptors)
    }

}