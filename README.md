# Advanced Android Interview Questions and Answers
<details>
<summary> How Android Push Notification System Works?</summary>

  Android Push notification System can be divided into 3 different layers. 
  1. Register
  2. Server-Side Trigger
  3. Client Side Delivery

  <b>Core Architecture</b> – Android uses a <code>Publish-Subscriber</code> pattern to receive notifications.
  To save battery, the device maintains a single persistent <b>TCP connection</b> to Google’s Push servers via
  <b>Google Play Services</b>. All apps on the device share this one connection.

  <b>Implementation</b>
  <ol>
    <li><b>Registration</b> – App requests FCM token and sends it to backend.</li>
    <li><b>Triggering</b> – Backend sends payload to FCM via HTTP v1 API.</li>
    <li><b>Delivery</b> – FCM routes message and wakes the app.</li>
  </ol>

  <b>Handling On Device</b>
  <ul>
    <li><b>Notification Messages:</b> System handled in background, callback in foreground.</li>
    <li><b>Data Messages:</b> Always delivered to <code>onMessageReceived()</code>.</li>
  </ul>
</details>

## Coroutines
<details>
<summary> What is a Coroutine? Why is it better than threads? </summary>

Coroutine is a "Lightweight-Thread". While normal threads are managed by operating system Coroutine is managed by Application Runtime. 

The word comes from **Co-operative + Routine**. Unlike threads, which are "pre-emptive" (the OS forces them to stop and start), coroutines cooperate by yielding control when they are waiting for something to finish.

I've noted that you'd like me to use Markdown (MD) format for my answers from now on, including support for tags within the Markdown.

### Why Coroutines are "Better" than Threads ?

While threads are powerful, they are expensive. Here is why coroutines are generally preferred for modern, high-concurrency applications:

1. **Resource Efficiency (Memory)**
Threads are heavy. Each thread typically reserves about 1MB of memory for its stack. If you try to launch 100,000 threads on a standard laptop, your system will crash. Coroutines are incredibly tiny. They only require a few kilobytes of memory. You can easily run hundreds of thousands of coroutines on a single device without breaking a sweat.
2. **Context Switching Overhead**
When the OS switches from one thread to another, it has to save the entire state of the CPU registers and reload the state for the next thread. This is called **Context Switching**, and it's a "heavy" operation that eats up CPU cycles. Switching between coroutines doesn't require the OS at all. It happens within the application code, making it nearly instantaneous.
3. **Non-Blocking Design (The "Suspend" Magic)**
In a traditional thread-based model, if a thread asks for data from a database, it blocks (sits idle) until the data arrives. That thread is stuck and can't do anything else. In a coroutine model, the coroutine **suspends**. It releases the underlying thread so that other tasks can use it. Once the data is ready, the coroutine resumes exactly where it left off.

</details>

<details>
<summary> What are Coroutine Builders? </summary>

In Kotlin, **coroutine builders** are the primary way to create and start a coroutine. They act as the entry point from regular functions into the world of suspension.


### 1. `launch`

The `launch` builder is used for **"fire and forget"** tasks. It starts a coroutine that performs work in the background without returning a specific result to the caller.

* **Returns:** A `Job` object (used to cancel the coroutine or check its status).
* **Analogy:** Sending an email; once you hit send, you move on to other tasks.

### 2. `async`

The `async` builder is used when you need to **calculate a result** asynchronously. It allows for parallel execution of tasks.

* **Returns:** A `Deferred<T>`, which is a non-blocking future.
* **Result:** You must call `.await()` to get the actual value once it is ready.
* **Analogy:** Ordering a pizza; you get a receipt (the `Deferred`) and eventually use it to get your pizza (the result).

### 3. `runBlocking`

This builder is a **bridge** between regular blocking code and the coroutine world. It blocks the current thread until the code inside finishes.

* **Returns:** The result of the code block.
* **Usage:** It should generally only be used in `main()` functions or unit tests. Never use it in production UI code as it will freeze the application.
* **Analogy:** A "stop" sign; everything waits until the task is complete.

### Summary Table

| Builder | Return Type | Use Case | Thread Impact |
| --- | --- | --- | --- |
| `launch` | `Job` | Background tasks with no result. | Non-blocking |
| `async` | `Deferred<T>` | Tasks that return a value. | Non-blocking |
| `runBlocking` | `T` | Testing or bridging code. | **Blocking** |

---

Would you like to see a code example of how to handle errors within these builders?

</details>

<details>
<summary>What's Dispatcher thread we should use and when?</summary>

In Kotlin Coroutines, the choice of **Dispatcher** depends entirely on the *type* of long-running operation you are performing. Long-running tasks generally fall into two categories: **CPU-bound** and **I/O-bound**.

### 1. For I/O-Bound Tasks: `Dispatchers.IO`

**Examples:** Network requests, reading/writing to a database, downloading files, or accessing the file system.

* **Why use it?** * **Elastic Thread Pool:** It is designed to offload blocking I/O operations to a shared pool of threads.
* **Thread Growth:** Unlike the CPU dispatcher, `Dispatchers.IO` can create additional threads (up to 64 or the number of CPU cores, whichever is larger) because I/O tasks often involve "waiting" (e.g., waiting for a server response). This prevents the entire system from starving while waiting for data.
* **Efficiency:** It ensures that your main thread remains responsive and your CPU-intensive threads aren't blocked by a slow hard drive.

### 2. For CPU-Bound Tasks: `Dispatchers.Default`

**Examples:** Sorting large lists, parsing complex JSON, image processing, or heavy mathematical calculations.

* **Why use it?** * **CPU Optimized:** This dispatcher is backed by a fixed pool of threads equal to the number of CPU cores available on the device.
* **No Over-Saturation:** Creating more threads than you have CPU cores for heavy math doesn't make things faster; it actually slows them down due to context switching. `Dispatchers.Default` ensures you are utilizing the hardware at its maximum efficiency without unnecessary overhead.

### 3. Comparison Summary

| Operation Type | Recommended Dispatcher | Reasoning |
| --- | --- | --- |
| **I/O-Bound** (Network/DB) | `Dispatchers.IO` | Optimized for waiting; can scale thread count to prevent blocking. |
| **CPU-Bound** (Math/Parsing) | `Dispatchers.Default` | Limited to CPU core count to maximize processing power per thread. |
| **UI Updates** | `Dispatchers.Main` | Mandatory for interacting with UI elements (Android/Swing/JavaFX). |

### Best Practice: The "Confined" Approach

Always specify the dispatcher inside the function that performs the work using `withContext`. This makes the function **main-safe**, meaning it can be called from any thread (including the Main thread) without crashing the app or lagging the UI.

```kotlin
suspend fun fetchUserData() = withContext(Dispatchers.IO) {
    // This long-running network call is now safe to call from the UI
    api.getUserProfile() 
}

```

Would you like me to explain how `withContext` differs from `launch` when switching dispatchers?

</details>

<details>
<summary>SupervisorScope vs SupervisorJob</summary>

# SupervisorScope vs. SupervisorJob

While both are used to handle **Structured Concurrency** and prevent a single failure from crashing an entire system, they are applied at different levels of the coroutine hierarchy.

---

### 1. SupervisorJob

A `SupervisorJob` is a specific type of **Job** that you can pass into a `CoroutineScope` or a coroutine context.

* **Failure Propagation:** Normally, if a child coroutine fails, it cancels its parent and all its siblings. A `SupervisorJob` changes this: a failure of a child **does not** result in the cancellation of the parent or other siblings.
* **Usage:** It is typically used when creating a custom scope (e.g., a background service that manages multiple independent tasks).
* **Key Detail:** You must handle exceptions manually (usually via a `CoroutineExceptionHandler`), or the exception will still reach the thread's uncaught exception handler.

### 2. SupervisorScope

`supervisorScope` is a **suspending function** that creates a new sub-scope with a `SupervisorJob`.

* **Scope Level:** It is used inside an existing coroutine to wrap a block of work.
* **Behavior:** It inherits the context from the outer scope but overrides the `Job` with a `SupervisorJob`. If any coroutine started *inside* the `supervisorScope` fails, the other coroutines inside that same scope continue to run.
* **Completion:** The `supervisorScope` function does not finish until all coroutines launched inside it are complete.

---

### Key Differences

| Feature | `SupervisorJob` | `supervisorScope` |
| --- | --- | --- |
| **Type** | A property/object (Class instance). | A suspending function (Block of code). |
| **Placement** | Usually defined in the `CoroutineContext`. | Used inside a coroutine to wrap sub-tasks. |
| **Lifecycle** | Stays active until manually cancelled or the scope is destroyed. | Ends when all children inside the block finish. |
| **Main Use Case** | Building a long-lived scope (like `viewModelScope`). | Running multiple independent, temporary tasks. |

---

### When to use which?

* **Use `SupervisorJob**` when you are defining a **global or long-lived scope** where you want the scope to survive even if individual background tasks fail.
* **Use `supervisorScope**` when you have a **specific function** that needs to run several concurrent tasks, and you don't want one failing task to stop the others in that specific group.

> **Crucial Warning:** If you pass a `SupervisorJob()` as an argument to `launch(SupervisorJob())`, it becomes the **parent** of that coroutine, but it is not linked to the outer scope. This often leads to memory leaks because the new coroutine is no longer a child of the original scope. Use `supervisorScope { ... }` instead for local tasks.

---

Would you like to see a code example where a standard `Job` crashes an app while a `SupervisorJob` keeps it running?

</details>

# Kotlin
<details>
<summary>What is SealedClass and Sealed Interface. How to use it? </summary>

# Sealed Classes and Sealed Interfaces

In Kotlin, **Sealed** classes and interfaces are used to represent **restricted class hierarchies**. They allow you to define a fixed set of types, providing more control over inheritance and enabling powerful features like exhaustive `when` expressions.

Think of them as "Enums on steroids." While an Enum constant is just a single instance, a subclass of a sealed class can have multiple instances and hold its own unique state.

---

### 1. Sealed Class

A `sealed class` is an abstract class that can only be subclassed within the same package/module where it is defined.

* **State:** Each subclass can have its own properties and methods.
* **Constructor:** It can have a constructor (private by default) to pass data to subclasses.
* **Usage:** Best used when your types share a common base with some logic or state.

### 2. Sealed Interface

Introduced in Kotlin 1.5, `sealed interface` works exactly like a sealed class but follows interface rules.

* **No State:** It cannot hold properties with backing fields (state).
* **Multiple Inheritance:** A class can implement multiple interfaces, but can only extend one class.
* **Usage:** Best used when you want to define a common behavior across different hierarchies or when you don't need a constructor.

---

### How to Use Them

The most common use case is representing **UI State** or **Result** types.

#### Code Example: Network Result

```kotlin
sealed interface DataResult {
    data class Success(val data: String) : DataResult
    data class Error(val message: String) : DataResult
    object Loading : DataResult
}

fun handleResult(result: DataResult) {
    // The compiler knows all possible types of DataResult
    // No 'else' branch is required!
    when (result) {
        is DataResult.Success -> println("Received: ${result.data}")
        is DataResult.Error -> println("Error: ${result.message}")
        DataResult.Loading -> println("Loading...")
    }
}

```

---

### Key Differences

| Feature | Sealed Class | Sealed Interface |
| --- | --- | --- |
| **Constructor** | Yes (can pass parameters) | No |
| **Inheritance** | Single inheritance only | Supports multiple implementations |
| **Backing Fields** | Can have stored properties | Only abstract properties/methods |
| **Primary Goal** | Restricted hierarchy with shared state | Restricted hierarchy for behavior/types |

---

### Why Use Them?

1. **Exhaustive `when`:** The compiler will warn you if you forget to handle one of the subclasses. This prevents bugs when adding new types later.
2. **Type Safety:** You know exactly which types belong to the hierarchy, making the code predictable.
3. **Domain Modeling:** Perfect for representing states like `Success`, `Error`, `Pending`, or `Idle`.

</details>

## Android 

<details>
<summary>What are the android lifecycle events ?</summary>

The Activity lifecycle defines how Android creates, displays, pauses, and destroys a screen. Correct usage ensures good performance, no leaks, and a smooth user experience.

- `onCreate()` : Called once when activity is created. best for , 
    - Dependency Injection 
    - Initializing  ViewModels
- `onStart()` : The Activity becomes visible but not yet interactive.
    - Good place for creating observers
- `onResume()` : The Activity is in the foreground and **fully interactive.**
    - Start Sensors, Camera or Location Updates.
    - Perform data Synchronization 
    - Resume animations or realtime updates
- `onPause()` : Called when activity is about to lose focus. 
    - Used to pause or suspend ongoing work.
    - Best place to pause Camera, Sensors, Listeners.
- `onStop()` : When activity is no longer visible. 
    - Used to release resources like Media Playback.

- `onDestroy()` : Final Cleanup when activity is destroyed.

</details>

<details>
<summary>How do you Handle Configuration Change ?</summary>

Configuration changes such as screen rotation , multi-window, foldable postures, changing locale cause the activity to be recreated, which means all Activity fields are lost. 

> **Core Principal** - UI State should not live inside activity.

There are 3 ways to save the data , 
- **ViewModel** :

    Hold the UI state in memory and survives configuration change. It has much longer   lifespan as the old viewModel can be reused inside new Activity.

- **SaveStateHandle** :

    Lightweight, Bundle backed storage. 
    Its fast but size is limited ~1MB.

- **Proto Datastore** :

    Disk based persistance. <br/>
    Survives **process death** (Force kill, low memory)<br/>
    Safest, ensure state is restored when relaunched.

</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

<details>
<summary></summary>
</details>

