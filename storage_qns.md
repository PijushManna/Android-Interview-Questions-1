## Common
### How to use the Scoped Storage System?

Scoped storage system is created in a privacy first way. Minimizing the access scope , ensuring consistancy across versions. 

### 1. App Specific Storage

Files for meant to be Private and not accessible via other apps. 
 - Internal Storage :  Files are always private and deleted when the app is uninstalled. Access via `context.getFilesDir()`.

 - External Storage : Files are stored in a specific directory.They will be removed on uninstall. Can be accessed via `context.getExternalFilesDir()`.

 ### 2. MediaStore - APIs

 For managing shared media files (images, audio, videos) that should be available in the user's gallery or other media apps, use the **MediaStore API**. 

- To save media: Contribute files to the appropriate media collections (e.g., `MediaStore.Images.Media.EXTERNAL_CONTENT_URI`). No special permissions are needed to add your own media files.

- To read other apps' media: Declare the `READ_EXTERNAL_STORAGE` permission in your `AndroidManifest.xml`.

- To modify or delete other apps' media: You must use explicit user consent via `MediaStore.createWriteRequest()` or `MediaStore.createDeleteRequest()`, which prompts the user to grant permission for a specific set of files.

- To access location info from photos: Declare the `ACCESS_MEDIA_LOCATION` permission in your manifest to read unredacted location metadata. 

### 3. Storage Access Framework - SAF

For non-media files (e.g., PDFs, documents) created by other apps, or to allow users to pick a specific directory, use the Storage Access Framework (SAF). This displays the system's file picker, giving the user control over which files/directories your app can access. 

- Open a specific document: Use the ACTION_OPEN_DOCUMENT intent.

- Select a directory for broader access: Use the ACTION_OPEN_DOCUMENT_TREE intent to request access to an entire directory and its subdirectories. You can persist this access across app restarts using contentResolver.takePersistableUriPermission().

- Create a new file in a user-specified location: Use the ACTION_CREATE_DOCUMENT intent. 

### How would you extract SMS/MMS/RCS safely?

For SMS/MMS, Android provides access via the Telephony content provider using READ_SMS permission, but access differs across versions and OEMs. MMS requires parsing from the MMS database tables and reconstructing parts.

RCS is trickier because Google Messages stores it privately, so direct extraction isn’t officially supported unless the OEM exposes it. In such cases, we must treat RCS as ‘best effort’ and only support what Android allows legally and technically.

## How do you manage app storage and ensure reliability?

As a senior Android developer, I manage storage by **choosing the right persistence layer**, enforcing **data integrity**, and designing for **failure and recovery**.

### 1. Choose the right storage for the use case
- **Room DB** → structured, relational, offline-first data
- **DataStore (Preferences/Proto)** → lightweight app and user settings
- **File system** → media, cached files, large blobs
- **In-memory cache** → temporary UI/state data

> I avoid overloading SharedPreferences and use DataStore for safer async access.

### 2. Ensure data integrity
- Use **Room transactions** for multi-step writes
- Apply **primary keys, indices, and constraints**
- Handle schema changes with **versioned migrations**
- Validate data before persisting

> I manage app storage by selecting the right persistence layer, enforcing data integrity with Room and transactions, designing offline-first sync, and using WorkManager to ensure reliable background execution even across failures.