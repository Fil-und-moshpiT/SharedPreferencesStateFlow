package com.filimonov.sharedpreferencesstateflow

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("OPT_IN_USAGE")
public abstract class SharedPreferencesStateFlow<T> internal constructor(
    private val preferences: SharedPreferences,
    private val key: String,
    internal val default: T
) {
    internal val flow: Flow<T> = callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == this@SharedPreferencesStateFlow.key) {
                trySend(getValueFromPreferences(key, default))
            }
        }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        awaitClose { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.conflate()

    public abstract fun getValueFromPreferences(key: String, default: T): T
}

public fun SharedPreferences.createStateFlow(key: String, default: String) : SharedPreferencesStateFlow<String> {
    return object : SharedPreferencesStateFlow<String>(this, key, default) {
        override fun getValueFromPreferences(key: String, default: String): String =
            this@createStateFlow.getString(key, default) ?: default
    }
}

public fun SharedPreferences.createStateFlow(key: String, default: Boolean) : SharedPreferencesStateFlow<Boolean> {
    return object : SharedPreferencesStateFlow<Boolean>(this, key, default) {
        override fun getValueFromPreferences(key: String, default: Boolean): Boolean =
            this@createStateFlow.getBoolean(key, default)
    }
}

public fun SharedPreferences.createStateFlow(key: String, default: Int) : SharedPreferencesStateFlow<Int> {
    return object : SharedPreferencesStateFlow<Int>(this, key, default) {
        override fun getValueFromPreferences(key: String, default: Int): Int =
            this@createStateFlow.getInt(key, default)
    }
}

public fun SharedPreferences.createStateFlow(key: String, default: Long) : SharedPreferencesStateFlow<Long> {
    return object : SharedPreferencesStateFlow<Long>(this, key, default) {
        override fun getValueFromPreferences(key: String, default: Long): Long =
            this@createStateFlow.getLong(key, default)
    }
}

public fun SharedPreferences.createStateFlow(key: String, default: Float) : SharedPreferencesStateFlow<Float> {
    return object : SharedPreferencesStateFlow<Float>(this, key, default) {
        override fun getValueFromPreferences(key: String, default: Float): Float =
            this@createStateFlow.getFloat(key, default)
    }
}

@Composable
public fun <T> SharedPreferencesStateFlow<T>.collectAsState(context: CoroutineContext = EmptyCoroutineContext): State<T> =
    flow.collectAsState(default, context)
