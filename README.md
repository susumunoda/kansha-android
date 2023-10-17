# Summary
In Japanese, _kansha_ (感謝) means gratitude. This app aims to be a way to increase gratitude in the user's life by providing the following functionality:
1. [MVP] Allow users to write notes in different categories for things that they are grateful for in life
2. Allow users to schedule reminders to take a moment to be grateful and, if they want to, write a note
3. Allow users to set and track goals for how often they want to integrate gratitude into their routines

# Platform
For the time being, this project is only being built for Android. An iOS version may be added in the future, but it is not currently planned.

# Modularization
Where possible, logic that is not specific to this app and that could possibly be useful in other apps has been extracted into separate libraries (i.e. GitHub repos). For example, the [`authcontroller` library](https://github.com/susumunoda/authcontroller) contains app-agnostic logic for managing authentication. As another example, this app uses Firebase's [Cloud Firestore](https://firebase.google.com/docs/firestore) for its backend, and the [`android-firebase-firestore` library](https://github.com/susumunoda/android-firebase-firestore) provides a useful abstraction over the barebones Android Firestore SDK.

Additionally, a large benefit of not tightly coupling the app with implementation-specific details of its components (e.g. by interacting with only interfaces throughout the codebase) is that changes to the internal implementation details will result in minimal to no changes in the rest of the application. For example, the `firebase-auth` library was [updated to support Kotlin Multiplatform](https://github.com/susumunoda/firebase-auth/commit/e2a85d3fcb0d9ea3d6cff6913c1d442cd2165089) — meaning its internals changed to rely on a completely different, KMP-compatible Firebase SDK — but the changes in this repository were little more than [updating package names and making a small tweak due to a minor API change](https://github.com/susumunoda/kansha-android/commit/05f106acfd697e7085e67dd9a5e391a07958eeec).

# Implementation
## UI
The UI for this app is built using a single-activity architecture with [Jetpack Compose](https://developer.android.com/jetpack/compose). To keep the code modular, each screen generally consists of a composable function that receives a view model (injected with Hilt), and the view model typically maintains state which is an instance of a `data class` for ease of [copying](https://kotlinlang.org/docs/data-classes.html#copying) during state updates. See [`AuthScreen`](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/screen/auth/AuthScreen.kt), [`AuthScreenViewModel`](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/screen/auth/AuthScreenViewModel.kt), and [`AuthScreenState`](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/screen/auth/AuthScreenState.kt) for examples.

## Navigation
Navigation is implemented with [Navigation Compose](https://developer.android.com/jetpack/compose/navigation). In addition, a [custom `compose-navigation` library](https://github.com/susumunoda/compose-navigation) includes useful utilities such as a [bottom navigation bar](https://github.com/susumunoda/compose-navigation/blob/main/library/src/main/java/com/susumunoda/compose/navigation/BottomNavBar.kt) with sensible default behavior.

Currently, the app has [two top-level `NavHost`s](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/KanshaApp.kt#L15-L21), one for an authenticated (logged in) state and the other for an unauthenticated (logged out) state. The main reason to do this was that the UI scaffolding differed substantially between the authenticated and unauthenticated states (e.g. no bottom nav bar when logged out) and that, currently, there is no intention to support navigating to a specific authenticated screen directly after being authenticated. This may change as features evolve.

In the authenticated flow, all navigation uses the same top-level `NavHost`, including nested navigation which is achieved with the [`NavGraphBuilder.navigation` extension function](https://developer.android.com/reference/kotlin/androidx/navigation/NavGraphBuilder#(androidx.navigation.NavGraphBuilder).navigation(kotlin.String,kotlin.String,kotlin.Function1)). See [`SettingsNavigation`](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/navigation/SettingsNavigation.kt) for an example.

## Authentication
Authentication is implemented with [Firebase Authentication](https://firebase.google.com/docs/auth). Session and user state is handled by a [custom `authcontroller` library](https://github.com/susumunoda/authcontroller) that provides an [`AuthController` interface](https://github.com/susumunoda/authcontroller/blob/main/authcontroller/src/commonMain/kotlin/com/susumunoda/auth/AuthController.kt) for observing changes to the session state via its `sessionFlow` property; this is useful for observing the current session/user in [view models](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/screen/notes/ViewCategoryScreenViewModel.kt#L48) and in [composable functions](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/KanshaApp.kt#L13). In addition, the [`SessionListenerHandler` class](https://github.com/susumunoda/authcontroller/blob/main/authcontroller/src/commonMain/kotlin/com/susumunoda/auth/SessionListenerHandler.kt) provides a hook for [performing an action on user login/logout](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/repository/category/FirebaseCategoryRepository.kt#L32-L47).

## Networking
In its current state, the app does not make raw network requests (e.g. with [Ktor](https://ktor.io/) or [Retrofit](https://square.github.io/retrofit/)) and instead relies on SDKs to make requests under the hood — for example, Firebase for auth and database queries and [Coil](https://coil-kt.github.io/coil/) for loading images.

## Persistence
Currently, this app requires a network connection as data is persisted using Firebase's [Cloud Firestore](https://firebase.google.com/docs/firestore). A decision was made fairly early to switch from Firebase [Realtime Database](https://firebase.google.com/docs/database) to Firestore for its more structured schemas and advanced querying capabilities.

## Dependency Injection
Dependency injection is handled by [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) with [kapt](https://kotlinlang.org/docs/kapt.html) for Java annotation processing. Note: It is an item on the backlog to [migrate from kapt to KSP](https://github.com/susumunoda/kansha-android/issues/4) as KSP support for Dagger/Hilt was [introduced in alpha state in Dagger 2.48](https://github.com/google/dagger/releases/tag/dagger-2.48).

## Asynchronous Programming
This project makes heavy use of [Kotlin coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for handling asynchronous code. In addition, [Kotlin flows](https://kotlinlang.org/docs/flow.html#flow-cancellation-basics) are used heavily for observing changes to data over time, such as [emitting state changes in view models](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/screen/notes/AddCategoryScreenViewModel.kt#L24C10-L29) and [observing the changed state in a composable function](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/screen/notes/AddCategoryScreen.kt#L51), or [observing a stream of realtime data over the network](https://github.com/susumunoda/kansha-android/blob/main/app/src/main/java/com/susumunoda/kansha/ui/screen/notes/ViewCategoryScreenViewModel.kt#L49-L70).

# Progress
As of now, the MVP of allowing users to create categories and notes is mostly complete. Still missing are the abilities to edit and delete categories and notes.

See [open issues](https://github.com/susumunoda/kansha-android/issues) for a complete list of remaining work items.

# Screenshots
<img width="283" alt="Screenshot 2023-10-17 at 11 52 26 AM" src="https://github.com/susumunoda/kansha-android/assets/5313576/d2006a9d-c367-466a-9421-55340e78c65b">
<img width="283" alt="Screenshot 2023-10-17 at 11 49 30 AM" src="https://github.com/susumunoda/kansha-android/assets/5313576/099843dc-fd80-4e4d-9995-6247066cc251">
<img width="283" alt="Screenshot 2023-10-17 at 11 52 04 AM" src="https://github.com/susumunoda/kansha-android/assets/5313576/7d4c494e-1e01-43a6-97fb-97795925bb3e">
<img width="283" alt="Screenshot 2023-10-17 at 11 57 49 AM" src="https://github.com/susumunoda/kansha-android/assets/5313576/2e7d71f3-fb30-4755-868a-daf6d9c6457b">
<img width="283" alt="Screenshot 2023-10-17 at 11 59 29 AM" src="https://github.com/susumunoda/kansha-android/assets/5313576/e23bd694-b1d7-4871-8f01-d4f4b4200fe4">

# Demos
https://github.com/susumunoda/kansha-android/assets/5313576/72733812-1a7a-4f1f-a836-dac5aefc52f9
