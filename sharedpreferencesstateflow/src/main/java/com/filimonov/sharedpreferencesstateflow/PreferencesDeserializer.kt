package com.filimonov.sharedpreferencesstateflow

public interface PreferencesDeserializer<T> {
    public fun deserialize(value: String) : T
}