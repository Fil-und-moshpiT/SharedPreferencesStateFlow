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
      implementation 'com.github.Fil-und-moshpiT:SharedPreferencesStateFlow:1.0.2'
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
