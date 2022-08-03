# Spring - Android CRUD example
An Android application which performs CRUD operations.

## The Books REST API
The Android application makes REST calls to the [Books REST API](https://github.com/kafousis/books-rest-api) and performs the following CRUD operations:
- Get saved books
- Create new book
- Update existing book
- Delete existing book

After running Books REST API you have to change the BASE_URL constant at [RestClient class](https://github.com/kafousis/spring-android-crud/blob/main/app/src/main/java/com/springcrud/android/rest/RestClient.java) according to the IP and Port you used to run it.

> If you run the Books REST API locally in your computer, be aware that the Android device should be in the same network.

## Software Stack
- Gradle
- Android SDK
- Retrofit
