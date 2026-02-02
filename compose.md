# Compose Questions

## What is a side effect?

A **side effect** is any operation that changes external state and is not part of UI rendering.

Examples:
- Network calls
- Database writes
- Navigation
- Showing Toast/Snackbar
- Logging analytics

In Jetpack Compose, UI should be **pure and declarative**, so side effects must be **explicitly controlled**.

## How do side effects work in Compose?

Jetpack Compose can **recompose multiple times** due to state changes or parent recompositions.  
Because of this, any operation that affects **external state** must be handled explicitly using **lifecycle-aware side-effect APIs** to avoid repeated or unintended execution.


### `LaunchedEffect` — Controlled async work

**Use when:** triggering async work based on state (API calls, data loading)

- Runs a coroutine tied to the composable lifecycle
- Executes **only when the key changes**
- Automatically cancelled when the composable leaves composition

```kotlin
@Composable
fun UserScreen(userId: String, viewModel: UserViewModel) {
    LaunchedEffect(userId) {
        viewModel.fetchUser(userId)
    }
}
```
> A side effect is any action that modifies external state outside UI rendering, and in Compose it’s handled using lifecycle-aware APIs like `LaunchedEffect` to ensure safe, predictable execution.

### `SideEffect` — Post-recomposition work

**Use when:** updating non-Compose state or lightweight logging

 - Runs after every successful recomposition

 - Must be fast and synchronous

```kotlin
@Composable
fun HomeScreen() {
    SideEffect {
        analytics.logScreenView("Home")
    }
}
```

### `DisposableEffect` — Setup and cleanup

Use when: managing lifecycle-bound resources (listeners, observers)

 - Runs when entering composition

 - Cleans up when leaving composition or when key changes

```kotlin
@Composable
fun LocationScreen(manager: LocationManager) {
    DisposableEffect(Unit) {
        manager.startUpdates()
        onDispose {
            manager.stopUpdates()
        }
    }
}

DisposableEffect(locationManager) {
    locationManager.startUpdates()
    onDispose { locationManager.stopUpdates() }
}
```

### How do you handle configuration change in Compose?
## How compose works internally?
## What is the lifecycle of a compose ?
### What is state management in compose ?
