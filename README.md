# Spring - Android CRUD example
An Android application which performs CRUD operations.

## The Books REST API
The Android application makes REST calls to the [Books REST API](https://github.com/kafousis/books-rest-api) and performs the following CRUD operations:
- Get saved books
- Create new book
- Update existing book
- Delete existing book

> In order for the application to be able to reach the Books REST API, the BASE_URL constant at [RestClient class](https://github.com/kafousis/spring-android-crud/blob/main/app/src/main/java/com/springcrud/android/rest/RestClient.java) should be changed according to the appropriate IP and port. Also, be aware that if the Books REST API runs locally on your computer, the Android device should be in the same network.

## Software Stack
- Gradle
- Android SDK
- Retrofit