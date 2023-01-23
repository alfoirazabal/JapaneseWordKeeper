package com.alfoirazabal.japanesewordkeeper.logic.wordstokenization.dictionary.domain.meaning

import android.graphics.Color

open class DefinitionMeaningDescriptor(
    val descriptor : Array<String>
) {

    class PaintColors (
        val foreground : Int,
        val background : Int
    )

    open fun getPaintColor() : PaintColors {
        return PaintColors(
            foreground = Color.BLACK,
            background = Color.WHITE
        )
    }

    override fun equals(other: Any?): Boolean {
        var equal = false
        if (other is DefinitionMeaningDescriptor) {
            equal = other.descriptor.contentEquals(this.descriptor)
        }
        return equal
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("(")
        for (x in descriptor.indices) {
            stringBuilder.append(descriptor[x])
            if (x != descriptor.size - 1) {
                stringBuilder.append(", ")
            }
        }
        stringBuilder.append(")")
        return stringBuilder.toString()
    }

}