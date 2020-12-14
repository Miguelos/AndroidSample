# Android Sample

[![kotlin](https://img.shields.io/badge/Kotlin-1.4.xx-blue)](https://kotlinlang.org/) [![Dagger](https://img.shields.io/badge/Dagger-Hilt-orange)](https://dagger.dev/hilt)


:construction: Under construccion! :construction:

Sample App for use and practise of different libraries.

For now it shows a list of Marvel Characters from Marvel API.

## Features and Libraries
* Clean Architecture with MVVM
* Jetpack Navigation
* Dynamic Feature Modules
* Video streaming with Exoplayer
* Dagger Hilt
* Kotlin Gradle DSL
* Room
* Lifecycle
* [Marvel API](https://developer.marvel.com/docs)

## Libraries
*   [Dagger Hilt](https://dagger.dev/hilt)
*   [RxJava 3](https://github.com/ReactiveX/RxJava)
*   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
*   [DFM navigation](https://developer.android.com/guide/navigation)
*   [Constraint Layout](https://developer.android.com/training/constraint-layout)
*   [Gradle](https://docs.gradle.org)
*   [Room](https://developer.android.com/topic/libraries/architecture/room)
*   [Retrofit](https://square.github.io/retrofit)
*   [Timber](https://github.com/JakeWharton/timber)
*   [Glide](https://github.com/bumptech/glide)

## Structure
* **Presentation**: Model View View Model pattern from the base sample.
* **Domain**: Holds all business logic. The domain layer starts with classes named *use cases* used by the application presenters. These *use cases* represent all the possible actions a developer can perform from the presentation layer.
* **Repository**: Repository pattern from the base sample.

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