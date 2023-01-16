package com.alfoirazabal.japanesewordkeeper.gui.helpers

import android.content.Context
import com.alfoirazabal.japanesewordkeeper.R
import com.alfoirazabal.japanesewordkeeper.db.entities.Phrase

class PhrasesSorter(val context: Context) {

    private val sharedPreferences = context.getSharedPreferences(
        context.getString(R.string.preference_file_name), Context.MODE_PRIVATE
    )
    private val sharedPreferencesEditor = sharedPreferences.edit()

    val sortableTypes = arrayOf(
        context.getString(R.string.date_created),
        context.getString(R.string.date_modified),
        context.getString(R.string.date_last_access)
    )

    private val sortableOrders = arrayOf(
        context.getString(R.string.sorting_order_ascending),
        context.getString(R.string.sorting_order_descending)
    )

    private fun sortAsc(phrases : MutableList<Phrase>) {
        val type = sharedPreferences.getString(
            context.getString(R.string.sorting_type), sortableTypes[0]
        )
        when (type) {
            context.getString(R.string.date_created) -> {
                phrases.sortBy { e -> e.dateCreated }
            }
            context.getString(R.string.date_modified) -> {
                phrases.sortBy { e -> e.dateModified }
            }
            context.getString(R.string.date_last_access) -> {
                phrases.sortBy { e -> e.dateLastAccessed }
            }
        }
    }

    private fun sortDesc(phrases: MutableList<Phrase>) {
        val type = sharedPreferences.getString(
            context.getString(R.string.sorting_type), sortableTypes[0]
        )
        when (type) {
            context.getString(R.string.date_created) -> {
                phrases.sortByDescending { e -> e.dateCreated }
            }
            context.getString(R.string.date_modified) -> {
                phrases.sortByDescending { e -> e.dateModified }
            }
            context.getString(R.string.date_last_access) -> {
                phrases.sortByDescending { e -> e.dateLastAccessed }
            }
        }
    }

    fun sort(phrases : MutableList<Phrase>) {
        val order = sharedPreferences.getString(
            context.getString(R.string.sorting_order), sortableOrders[0]
        )
        when (order) {
            context.getString(R.string.sorting_order_ascending) -> {
                sortAsc(phrases)
            }
            context.getString(R.string.sorting_order_descending) -> {
                sortDesc(phrases)
            }
        }
    }

    fun setType(type : String) {
        sharedPreferencesEditor.putString(context.getString(R.string.sorting_type), type)
        sharedPreferencesEditor.apply()
    }

    fun setOrder(order : String) {
        sharedPreferencesEditor.putString(context.getString(R.string.sorting_order), order)
        sharedPreferencesEditor.apply()
    }

    fun getType() : String {
        return sharedPreferences.getString(context.getString(R.string.sorting_type), sortableTypes[0])!!
    }

    fun getOrder() : String {
        return sharedPreferences.getString(context.getString(R.string.sorting_order), sortableOrders[0])!!
    }

}