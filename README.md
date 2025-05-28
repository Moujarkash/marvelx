# MarvelX

A Kotlin Multiplatform Mobile (KMM) application that showcases Marvel characters and comics using the Marvel API. Built with Compose Multiplatform for both Android and iOS.

## Features

- Browse Marvel characters
- View character details
- Search functionality
- Cross-platform (Android & iOS) support

## Prerequisites

- Android Studio Hedgehog | 2023.1.1 or later
- Xcode 15.0 or later (for iOS development)
- Kotlin 1.9.0 or later
- JDK 17 or later

## Setup

1. Clone the repository:
```bash
git clone https://github.com/yourusername/MarvelX.git
cd MarvelX
```

2. Create a `local.properties` file in the root directory with your Marvel API credentials:
```properties
API_KEY=your_public_api_key_here
PRIVATE_KEY=your_private_key_here
```

To obtain these keys:
1. Go to [Marvel Developer Portal](https://developer.marvel.com/)
2. Create an account or sign in
3. Navigate to your account dashboard
4. Generate new API keys

## Building the Project

### Android
1. Open the project in Android Studio
2. Sync the project with Gradle files
3. Run the `composeApp` configuration

### iOS
1. Open the project in Xcode
2. Open the `iosApp` directory
3. Build and run the project

## Project Structure

- `composeApp/` - Android application module
- `iosApp/` - iOS application module
- `shared/` - Shared Kotlin code between platforms

## Technologies Used

- Kotlin Multiplatform Mobile (KMM)
- Compose Multiplatform
- Marvel API
- Kotlin Coroutines
- Ktor Client
- Kotlinx Serialization

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

This is a Kotlin Multiplatform project targeting Android, iOS.

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that's common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple's CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains the iOS application entry point. Since we're using Compose Multiplatform,
  the UI is shared between Android and iOS platforms.

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…