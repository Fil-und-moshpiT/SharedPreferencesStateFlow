package com.filimonov.sharedpreferencesstateflow

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.FlowCollector
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

public fun <T> SharedPreferences.createStateFlow(
    key: String,
    default: T,
    deserializer: PreferencesDeserializer<T>
) : SharedPreferencesStateFlow<T> {
    return object : SharedPreferencesStateFlow<T>(this, key, default) {
        override fun getValueFromPreferences(key: String, default: T): T =
            this@createStateFlow.getString(key, null).let {
                return@let if (it == null || it.isNullOrEmpty()) default else deserializer.deserialize(it)
            }
    }
}

@Composable
public fun <T> SharedPreferencesStateFlow<T>.collectAsState(
    context: CoroutineContext = EmptyCoroutineContext
): State<T> = flow.collectAsState(default, context)

@OptIn(InternalCoroutinesApi::class)
public suspend fun <T> SharedPreferencesStateFlow<T>.collect(collector: FlowCollector<T>) {
    return flow.collect(collector = collector)
}
