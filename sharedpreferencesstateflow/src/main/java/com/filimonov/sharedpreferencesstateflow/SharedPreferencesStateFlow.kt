package com.filimonov.sharedpreferencesstateflow

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate

@Suppress("OPT_IN_USAGE")
public abstract class SharedPreferencesStateFlow<T> internal constructor(
    private val preferences: SharedPreferences,
    private val key: String,
    internal val default: T
) {
    internal val flow: Flow<T> = callbackFlow {
        trySend(default)
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
