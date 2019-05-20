package com.makki.languageapp.lib.utils

import android.content.Context
import androidx.core.content.ContextCompat
import java.util.*

/**
 * @author Maksym.Popovych
 */
fun Int.asResourceString(context: Context, vararg arguments: Any) = context.resources.getString(this, *arguments)

fun Int.asResourceInt(context: Context) = context.resources.getInteger(this)

fun Int.asResourceIntArray(context: Context) = context.resources.getIntArray(this)

fun Int.asResourceStringArray(context: Context) = context.resources.getStringArray(this)

fun Int.asResourceBoolean(context: Context) = context.resources.getBoolean(this)

fun Int.asColor(context: Context) = ContextCompat.getColor(context, this)


/**
 * COLLECTIONS
 */

inline fun <T> List<T>?.takeIfEmpty(block: () -> List<T>) : List<T>{
    if (isNullOrEmpty()) return block()
    return this ?: emptyList()
}

fun <T> Collection<T>?.isNullOrEmpty() = this == null || isEmpty()

fun <T> Set<T>?.ensure() = this ?: Collections.emptySet()
fun <T> List<T>?.ensure() = this ?: Collections.emptyList()
fun <K, V> Map<K, V>?.ensure() = this ?: Collections.emptyMap()

fun <T> Collection<T>.asArrayList() = this as? ArrayList<T> ?: ArrayList(this)
fun <T> Collection<T>.asMutableList() = this as? MutableList<T> ?: ArrayList(this)

fun <T> List<List<T>>.merge() = let { original ->
    ArrayList<T>().also { final -> original.forEach { final.addAll(it) } }
}

fun <T> List<T>.safeSlice(indices: IntRange): List<T> {
    val newRange = IntRange(kotlin.math.max(indices.first, 0), kotlin.math.min(indices.last, size - 1))
    return this.slice(newRange)
}

fun <K, V> Map<K, V>.getSynced(key: K) = synchronized(this) { get(key) }
fun <K, V> MutableMap<K, V>.getSynced(key: K, fallback: V) = synchronized(this) {
    return@synchronized get(key) ?: fallback.also { put(key, fallback) }
}
inline fun <K, V> MutableMap<K, V>.getSynced(key: K, block: () -> V) = synchronized(this) {
    return@synchronized get(key) ?: block().also { put(key, it) }
}

inline fun <K, T> Iterable<T>.associateToMultimap(block: (T) -> K): Map<K, ArrayList<T>> {
    val map = HashMap<K, ArrayList<T>>()
    for (item in this) {
        map.getOrPut(block(item)) { ArrayList() }.add(item)
    }
    return map
}