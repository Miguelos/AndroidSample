# Android Sample

[![kotlin](https://img.shields.io/badge/Kotlin-1.4.xx-blue)](https://kotlinlang.org/) [![Dagger](https://img.shields.io/badge/Dagger-Hilt-orange)](https://dagger.dev/hilt)


:construction: Under construction! :construction:


Sample App for use and practise of different libraries.

For now it shows a list of Marvel Characters from Marvel API.

Includes:
 * Local Database for items showed in the ranking list.
 * Examples of UI testing and Unit testing.

### UI/Instrumentation tests

Under androidTest folder there are UI tests for the app happy paths. Here is the example of two 
tests for online and offline cases placed inside `MainActivityTest.kt`

```Kotlin
    /**
     * Check that without Internet connection an alert is shown to the user when searching for
     * characters.
     */
    @Test
    fun arenaFightHappyPath() {
        robot.turnOnInternetConnections()
        robot.clickButton(R.id.arena_button)
        robot.clickButton(R.id.select_fighters_b)
        robot.fillEditTextAndApply(R.id.search_et, "lo")
        robot.clickNthView(R.id.characters_rv, R.id.list_item_cb, 0)
        robot.clickNthView(R.id.characters_rv, R.id.list_item_cb, 1)
        robot.clickButton(R.id.select_list_items_b)
        robot.clickButton(R.id.fight_b)
        robot.turnOffInternetConnections()
        robot.clickButton(R.id.go_to_ranking_b)
        robot.assertOnView(withId(R.id.list_item_name_tv))
        robot.turnOnInternetConnections()
    }

    /**
     * Check that without Internet the Ranking shows previous characters involved in battles
     */
    @Test
    fun offlineSearchList() {
        robot.turnOffInternetConnections()
        robot.clickButton(R.id.list_button)
        robot.fillEditTextAndApply(R.id.search_et, "loki")
        robot.assertOnView(withText(R.string.dialog_offline))
        robot.turnOnInternetConnections()
    }
```


## Features and Libraries
* Clean Architecture with MVVM
* Single Activity Design
* Jetpack Navigation
* Dagger Hilt
* Kotlin Gradle DSL
* Room
* Lifecycle
* [Marvel API](https://developer.marvel.com/docs)

## Libraries
*   [Dagger Hilt](https://dagger.dev/hilt)
*   [Hilt Jetpack](https://developer.android.com/training/dependency-injection/hilt-jetpack)
*   [RxJava 3](https://github.com/ReactiveX/RxJava)
*   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
*   [DFM navigation](https://developer.android.com/guide/navigation)
*   [Constraint Layout](https://developer.android.com/training/constraint-layout)
*   [Gradle](https://docs.gradle.org)
*   [Room](https://developer.android.com/topic/libraries/architecture/room)
*   [Retrofit](https://square.github.io/retrofit). A type-safe HTTP client for Android and Java
*   [Timber](https://github.com/JakeWharton/timber)
*   [Glide](https://github.com/bumptech/glide)
*   [Mockito](https://github.com/mockito/mockito)
*   [LeakCanary](https://square.github.io/leakcanary). Memory leak detection library for Android

## Structure
* **Presentation**: Model View View Model pattern from the base sample.
* **Domain**: Holds all business logic. The domain layer starts with classes named *use cases* used by the application presenters. These *use cases* represent all the possible actions a developer can perform from the presentation layer.
* **Repository**: Repository pattern from the base sample.

## Setup

It is needed to add Marvel API keys to gradle in order to build the app.

	marvelApiPublicKey
	marvelApiPrivateKey


## Author
Miguel González Pérez

## License
	Copyright 2020 Miguel González Pérez

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

		http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
