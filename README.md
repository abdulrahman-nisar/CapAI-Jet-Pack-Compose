# CapAI - AI-Powered Caption Generator 🎨✨

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Firebase](https://img.shields.io/badge/AI-Firebase%20Gemini-orange.svg)](https://firebase.google.com/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen.svg)](https://developer.android.com/jetpack/compose)

CapAI is an intelligent Android application that leverages Google's Firebase Gemini AI to automatically generate platform-specific captions for your images. Whether you're posting to Instagram, Facebook, Twitter, LinkedIn, or any other social media platform, CapAI creates engaging, context-aware captions tailored to each platform's unique style.

---

## 📱 Features

### Core Functionality
- **AI-Powered Caption Generation**: Uses Firebase Gemini AI to analyze images and generate contextually relevant captions
- **Multi-Platform Support**: Generates optimized captions for 8 different social media platforms:
  - 📸 Instagram
  - 👥 Facebook
  - 🐦 Twitter/X
  - 📌 Pinterest
  - 💼 LinkedIn
  - 🧵 Threads
  - 👻 Snapchat
  - 🎵 TikTok

### User Experience
- **Customizable Caption Length**: Choose between short and long caption formats
- **Caption History**: View and manage previously generated captions
- **Easy Sharing**: One-tap sharing of images with captions to social media
- **Material Design 3**: Modern, intuitive UI following Material You design guidelines
- **Splash Screen**: Polished app launch experience
- **Edge-to-Edge Display**: Full-screen immersive experience

### Technical Features
- **Offline Storage**: Local database using Room for caption history
- **Image Handling**: Support for various image formats with efficient bitmap processing
- **Dependency Injection**: Clean architecture with Hilt/Dagger
- **MVVM Architecture**: Separation of concerns with ViewModel pattern
- **Type-Safe Navigation**: Navigation3 with Kotlin Serialization
- **Reactive UI**: StateFlow for reactive state management

---

## 🏗️ Architecture

The app follows **Clean Architecture** principles with clear separation of layers:

```
CapAI/
├── data/                   # Data Layer
│   ├── local/             # Local data sources
│   │   ├── dao/           # Room DAOs
│   │   ├── database/      # Room Database
│   │   └── entity/        # Database entities
│   ├── remote/            # Remote data sources
│   │   └── CapAIGeminiFireBase.kt
│   └── repository/        # Repository implementations
│
├── domain/                 # Domain Layer
│   ├── model/             # Domain models
│   ├── repository/        # Repository interfaces
│   └── usecase/           # Business logic use cases
│
├── ui/                     # Presentation Layer
│   ├── screen/            # Composable screens
│   │   ├── components/    # Reusable UI components
│   │   ├── HomeScreen.kt
│   │   ├── SelectImageScreen.kt
│   │   ├── CaptionPreferencesScreen.kt
│   │   ├── DetailsScreen.kt
│   │   └── HomeDetailsScreen.kt
│   ├── navigation/        # Navigation setup
│   ├── theme/             # App theming
│   └── CapAiViewModel.kt  # Main ViewModel
│
└── di/                     # Dependency Injection
    ├── DatabaseModule.kt
    └── RepositoryModule.kt
```

### Key Architecture Patterns
- **MVVM (Model-View-ViewModel)**: For presentation layer
- **Repository Pattern**: For data abstraction
- **Use Case Pattern**: For business logic encapsulation
- **Dependency Injection**: Using Hilt for loose coupling

---

## 🛠️ Tech Stack

### Languages & Frameworks
- **Kotlin** (v2.0.21) - Primary programming language
- **Jetpack Compose** - Modern declarative UI framework
- **Material Design 3** - UI components and theming

### Android Architecture Components
- **ViewModel** - UI state management
- **Room Database** (v2.8.4) - Local data persistence
- **Navigation3** (v1.1.0-alpha02) - Type-safe navigation
- **StateFlow** - Reactive state management
- **Lifecycle Components** - Lifecycle-aware components

### Dependency Injection
- **Hilt/Dagger** (v2.56.2) - Dependency injection framework

### Firebase Services
- **Firebase AI (Gemini)** - AI-powered image captioning
- **Firebase Analytics** - Usage analytics

### Build Tools & Plugins
- **Gradle** (v8.11.1) with Kotlin DSL
- **KSP** (v2.0.21-1.0.27) - Annotation processing
- **Android Gradle Plugin** (v8.11.1)

### Image Processing
- **Coil** (v2.6.0) - Async image loading for Compose

### Serialization
- **Kotlinx Serialization** (v1.7.3) - JSON serialization

### Testing
- **JUnit** - Unit testing
- **Espresso** - UI testing

---

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Android Studio**: Iguana | 2024.1.1 or later
- **JDK**: Java 11 or higher
- **Android SDK**: API 24 (Android 7.0) or higher
- **Firebase Project**: Set up with Gemini AI enabled

### Minimum Requirements
- **Min SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36
- **Compile SDK**: 36

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/yourusername/CapAi.git
cd CapAi
```

### 2. Firebase Setup

1. Create a new Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app to your Firebase project
   - Package name: `com.example.capai`
3. Download the `google-services.json` file
4. Place it in the `app/` directory of the project
5. Enable Firebase AI (Gemini) in your Firebase project

### 3. Build Configuration

The project uses Gradle with Kotlin DSL. No additional configuration should be needed if Firebase is set up correctly.

### 4. Build and Run

**Using Android Studio:**
1. Open the project in Android Studio
2. Wait for Gradle sync to complete
3. Click "Run" or press `Shift + F10`

**Using Command Line:**

```bash
# For Windows
.\gradlew.bat assembleDebug

# For Linux/Mac
./gradlew assembleDebug
```

---

## 📖 Usage

### Generating Captions

1. **Launch the App**: Open CapAI on your Android device
2. **Select Image**: Tap the "+" button on the home screen to select an image
3. **Choose Caption Length**: Select between "Short" or "Long" caption format
4. **Generate**: Tap "Generate Caption" to process the image
5. **Review**: View platform-specific captions generated for different social media
6. **Share**: Tap the share button on any caption to share directly

### Viewing History

- Access caption history from the home screen
- Tap on any previous caption to view details
- Delete unwanted captions by swiping

### Navigation Drawer

- Access app settings and information
- View about section
- Clear history (if implemented)

---

## Demo

[<!-- Add screenshots here when available -->
*Screenshots coming soon*
](https://github.com/user-attachments/assets/912ba610-3b2d-465f-9858-9cfe49b6aaa3)

---

## 🔑 Key Components

### ViewModel
```kotlin
CapAiViewModel
├── getImageCaption()       # Generate captions using AI
├── addCaptionToHistory()   # Save to local database
├── getHistoryList()        # Retrieve saved captions
└── deleteCaptionFromHistory() # Remove captions
```

### Use Cases
- **GetImageCapUseCase**: Handles AI caption generation
- **RoomDatabaseOperationsUseCase**: Manages local data operations

### Data Models
- **CapAI**: Main domain model containing captions for all platforms
- **Length**: Enum for caption length (SHORT, LONG)
- **CaptionResult**: Wrapper for generation state and results

---

## 🔧 Configuration

### Modifying Caption Prompts

Caption generation logic is in `CapAIGeminiFireBase.kt`. Customize prompts in the `_generateResult()` method to adjust caption style and tone.

### Adding New Platforms

1. Add new field to `CapAI` data class
2. Update `CaptionEntity` for database
3. Add generation call in `geminiImageToCaption()`
4. Create UI component for the new platform

---

## 📦 Dependencies

### Production Dependencies

```kotlin
// Core Android
androidx.core:core-ktx:1.16.0
androidx.lifecycle:lifecycle-runtime-ktx:2.9.2
androidx.activity:activity-compose:1.10.1

// Jetpack Compose
androidx.compose:compose-bom:2024.09.00
androidx.compose.ui:ui:1.10.1
androidx.compose.material3:material3:1.4.0
androidx.compose.material:material-icons-extended

// Room Database
androidx.room:room-runtime:2.8.4
androidx.room:room-compiler:2.8.4 (KSP)

// Hilt/Dagger
com.google.dagger:hilt-android:2.56.2
com.google.dagger:hilt-android-compiler:2.56.2 (KSP)

// Firebase
com.google.firebase:firebase-bom:34.0.0
com.google.firebase:firebase-analytics
com.google.firebase:firebase-ai

// Navigation
androidx.navigation3:navigation3-runtime:1.1.0-alpha02
androidx.navigation3:navigation3-ui:1.1.0-alpha02

// Image Loading
io.coil-kt:coil-compose:2.6.0

// Serialization
org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3

// Splash Screen
androidx.core:core-splashscreen:1.0.1
```

### Gradle Plugins

```kotlin
com.android.application:8.11.1
org.jetbrains.kotlin.android:2.0.21
org.jetbrains.kotlin.plugin.compose:2.0.21
com.google.gms.google-services:4.4.3
com.google.dagger.hilt.android:2.56.2
com.google.devtools.ksp:2.0.21-1.0.27
org.jetbrains.kotlin.plugin.serialization:2.0.21
```

---

## 🧪 Testing

### Running Unit Tests

```bash
.\gradlew.bat test
```

### Running Instrumented Tests

```bash
.\gradlew.bat connectedAndroidTest
```

---

## 🐛 Known Issues

- Requires Android API 24 or higher
- Firebase Gemini AI requires active internet connection
- Large images may take longer to process

---

## 🗺️ Roadmap

### Planned Features
- [ ] Custom caption templates
- [ ] Hashtag generation
- [ ] Multiple language support
- [ ] Batch image processing
- [ ] Caption editing and customization
- [ ] Export captions to clipboard
- [ ] Dark/Light theme toggle
- [ ] Caption analytics and insights

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style
- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features

---


## 👨‍💻 Author

**Your Name**
- GitHub: [abdulrahman-nisar](https://github.com/abdulrahman-nisar)
- Email: abdulrahmannisar6@gmail.com

---

## 🙏 Acknowledgments

- **Google Firebase** for Gemini AI API
- **Jetpack Compose** team for the amazing UI toolkit
- **Material Design** for design guidelines
- **Android Developer Community** for continuous support

---

## 🔐 Privacy & Security

- Images are processed locally and sent to Firebase AI for caption generation
- No images are stored on external servers permanently
- All caption history is stored locally on the device
- Firebase Analytics collects anonymized usage data

---

<p align="center">Made with ❤️ using Kotlin and Jetpack Compose</p>
