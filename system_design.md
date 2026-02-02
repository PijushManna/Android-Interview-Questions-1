## A new feature must be added to your modular system, but it will heavily increase inter-module communication. How would you redesign the architecture to maintain scalability and minimize coupling? 

If a new feature significantly increases inter-module communication, my goal is to **preserve modular boundaries and reduce direct dependencies**, not let modules talk to each other freely.


### My approach

### 1. Introduce a clear contract layer
- Define **interfaces or contracts** in a shared `core` or `domain` module.
- Feature modules depend only on **contracts**, not on each other’s implementations.

> This ensures modules communicate through **abstractions**, not concrete classes.



### 2. Use unidirectional data flow
- Data flows **into a feature**, not across features.
- Shared state or events are exposed via:
  - `UseCases`
  - `SharedFlow`
  - `Repository interfaces`

> This avoids cyclic dependencies and keeps ownership clear.



### 3. Apply event-based communication
- Replace direct calls with **event-driven communication**.
- Use:
  - `SharedFlow` / `Channel` for in-app events
  - Navigation events via a centralized coordinator

> Modules emit events; interested modules react — no tight coupling.



### 4. Re-evaluate module boundaries
- If communication is excessive, it’s usually a **design smell**.
- I check whether:
  - The feature should be its **own module**
  - Shared logic should move to a **new common module**

> Scalability improves when responsibilities are aligned, not forced.



### 5. Dependency rule enforcement
- Enforce **one-way dependencies** (feature → domain → core).
- Prevent feature-to-feature dependencies using Gradle rules.


> When inter-module communication grows, I redesign the system around contracts, unidirectional data flow, and event-based communication, ensuring modules depend on abstractions—not each other—so scalability and maintainability remain intact.



#### Key takeaway
```
More communication ≠ tighter coupling.  
Better abstractions = scalable architecture.
```