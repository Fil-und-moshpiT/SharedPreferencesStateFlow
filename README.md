SharedPreferencesStateFlow [![](https://jitpack.io/v/Fil-und-moshpiT/SharedPreferencesStateFlow.svg)](https://jitpack.io/#Fil-und-moshpiT/SharedPreferencesStateFlow)
==================================
Android library that allows you to use SharedPreferences as StateFlow

Supports XML and Jetpack Compose

How to add to your project
--------------
Step 1. Add the JitPack repository to your root build.gradle at the end of repositories:
````groovy
  allprojects {
      repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
  }
````

Step 2. Add the dependency
````groovy
  dependencies {
      implementation 'com.github.Fil-und-moshpiT:SharedPreferencesStateFlow:1.0.0'
  }
````

How to use
--------------
With SharedPreferences instance call:
````kotlin
val flow = preferences.createStateFlow(KEY, default)
````
Then you can use that flow in Composable:
````kotlin
@Composable
fun HelloWorld(flow: SharedPreferencesStateFlow<String>) {
    val text by flow.collectAsState()
    Text(text = text)
}
````
Or use it in XML Fragment/Activity:
````kotlin
lifecycleScope.launchWhenCreated {
    flow.collect { binding.textString.text = it }
}
````

Supported types
--------------
````kotlin
// Boolean
val booleanFlow = preferences.createStateFlow(key = "BOOLEAN_KEY", default = false)

// Integer
val integerFlow = preferences.createStateFlow(key = "INTEGER_KEY", default = 0)

// Long
val longFlow = preferences.createStateFlow(key = "LONG_KEY", default = 0L)

// Float
val floatFlow = preferences.createStateFlow(key = "FLOAT_KEY", default = .0f)

// String
val stringFlow = preferences.createStateFlow(key = "STRING_KEY", default = "")

// Object
// You should implement PreferencesDeserializer
// For example, using Gson
val deserializer = object : PreferencesDeserializer<SomeClass> {
    override fun deserialize(value: String): SomeClass {
        return Gson().fromJson(value, SomeClass::class.java)
    }
}

val objectFlow = preferences.createStateFlow(key = "OBJECT_KEY", default = SomeClass(1), deserializer = deserializer)
````
