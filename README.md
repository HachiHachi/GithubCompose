# GithubCompose-App
This is a android app and create by using GitHub API <a href = "https://docs.github.com/zh/rest/users?apiVersion=2022-11-28#get-a-single-user">(API) </a>. 
also Use Retrofit for get JSON data and send request.

  - it list Limit to 100 users
  - Use Jetpack Compose to implement application

Used Library
  - Retrofit for get JSON data and send request <a href = "https://github.com/square/retrofit">Retrofit
  - Dagger for inject viewmodel <a href = "https://github.com/google/dagger">Dagger
  - Coil for display image <a href = "https://github.com/coil-kt/coil">Coil

Structure
- MVVM + Kotlin
- Custom ApiErrorException, Error handling can be easily extended.
- Repository can be easily to extended mock environment.
- Custom Interceptor, Decrypt and Encrypt can be easily extended.
