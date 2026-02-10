suspend fun fetchUserData() = withContext(Dispatchers.IO) {
    // This long-running network call is now safe to call from the UI
    api.getUserProfile() 
}
