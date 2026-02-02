# Behavioural Questions
## 1. Tell me about yourself ?

Answer : I am a Senior Android Developer with ~X years of experience building production grade apps in Kotlin , Java and Compose. I have worked with apps where device fragmentation, background execution limitations and performance is a real problem. 

What excites me about this role is - It doesn't require normal feature delivery but needs Strong Architectural Expertise, Product Ownership and Deep Level OS understanding. 

## 2. Tell me about challenges you solve ?

Answer: I have hit multiple cases where same feature behaves differently accross different OEMs, OS Version, Even when app is in the foreground.

My Approach is ,- 
- Reproduce the logs about OEMs, OS Versions, Battery Settings
- Implement fallback strategies
- Introduce Alarms for same scenarios
- Monitor via Crashlytics / Datadog

## 3. How do you manage the Performance of your app ?

I optimize app performance accross 5 key areas,

1. **Measure First** : 
    - I Measure the performance of the app via Crashlytics & Firebase Performance monitoring to indentify the bottlenecks and create a baseline

2. **Main Thread Discipline**:
    - I ensure no heavy work should be done on the main thread.
    - Offloading, Parsing, DB, Analytics, IO, ads and File Operations should be done using Coroutines, Flows , Work Manager

3. **Efficient Architecture**:
    - I Follow MVVM / MVI with clear separation.
    - I use immutable state with clear separation of concern to avoid unecessary redraws. 

4. **Network Usage**:
    - Optimize network usage by caching, exponential back off retries, batching.

5. **Memory Leaks**:
    - Prevent memory leaks with lazy load heavy objects. 
    - Paginate large list.

> “I treat performance as a system problem—measured in production, enforced by architecture, and continuously optimized where it impacts users and revenue.”


## ⭐ STAR Answer: How do you manage app performance?

### **S – Situation**
At **PocketFM**, we observed **UI jank and delayed interactions** during audio playback and ads, which negatively impacted **user experience and monetization** at scale.

### **T – Task**
My responsibility was to **identify performance bottlenecks** and ensure that critical user flows remained **smooth, responsive, and stable** in production.

### **A – Action**
- Measured real issues using **Firebase Performance Monitoring** and **Crashlytics** (ANRs, slow screens).
- Moved heavy tracking and ads logic off the main thread using **Kotlin Coroutines, Flows, and WorkManager**.
- Optimized network calls through **batching, caching, and retry strategies**.
- Improved UI performance by refining **Compose/MVVM state handling** to avoid unnecessary recompositions.

### **R – Result**
- Achieved **30% improvement** in tracking module performance.
- Reduced memory usage by **20%**.
- Increased ads success rate to **98%**.
- Contributed to **$100K+ revenue impact** with a smoother, crash-free experience.

### **One-line Closer**
> *I manage performance by measuring in production, enforcing main-thread discipline through architecture, and continuously optimizing the paths that directly affect users and revenue.*

## How do you manage scalability and maintainability of your app?

I focus on **clean architecture, modularization, and strict separation of concerns** so the app can grow without becoming hard to change.

### 1. Architecture-first approach
- Use **MVVM / MVI with Clean Architecture**
- Separate **UI, domain, and data layers**
- Keep business logic out of UI

> This allows features to scale independently and reduces regression risk.

### 2. Modularization
- Split the app into **feature modules** and **core modules**
- Isolate shared logic (network, DB, analytics)
- Enables parallel development and faster builds

### 3. Single source of truth
- UI observes **state from ViewModel**
- Data flows in one direction (DB / API → UI)
- Prevents inconsistent states as the app grows

### 4. Dependency management
- Use **Hilt/Dagger** for dependency injection
- Rely on interfaces, not implementations 
- Makes testing, refactoring, and scaling safer

### 5. Scalable UI patterns
- Use **Jetpack Compose** with state hoisting
- Keep composables small, reusable, and stateless
- Avoid tightly coupled UI logic

### 6. Maintainability practices
- Consistent coding standards and code reviews
- Meaningful naming and clear abstractions
- Automated testing for critical business flows

> I manage scalability and maintainability by using clean architecture, modularizing features, enforcing unidirectional data flow, and keeping UI and business logic strictly separated so the app can evolve without breaking existing functionality.



## Interview Answer: Bridging communication gaps in a cross-functional, multicultural team

When cultural differences cause misinterpretation, I address it by **creating clarity, standardizing communication, and building psychological safety**, rather than relying on assumptions.



## My approach

### 1. Establish clear communication standards
- Encourage **written, structured updates** (What changed, Impact, Next steps).
- Avoid ambiguous language and assumptions.
- Document decisions and action items after every discussion.

> This removes dependency on individual communication styles.



### 2. Make context explicit
- Ask team members to include **intent and rationale**, not just status.
- Encourage “*why* before *what*” in updates.

> This prevents misinterpretation caused by cultural or language differences.



### 3. Normalize clarification, not agreement
- Actively promote asking clarifying questions.
- Reinforce that clarification is not confrontation.

> This creates psychological safety and reduces silent misunderstandings.



### 4. Use async-first communication
- Prefer async tools (docs, tickets, threads) over real-time-only discussions.
- Gives everyone time to process, especially across time zones.



### 5. Align regularly across functions
- Short, focused syncs to realign goals and expectations.
- Summarize outcomes in writing to avoid drift.



## One-line interview answer

> I proactively reduce cultural communication gaps by standardizing written updates, making context and intent explicit, encouraging clarification, and reinforcing async-first communication to ensure alignment across diverse teams.



## Key takeaway

**Clarity scales better than conversation style.  
Structure removes cultural ambiguity.**