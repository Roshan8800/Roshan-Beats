# Roshan Beats - Music Streaming App

Roshan Beats is a modern, feature-rich music streaming application for Android, designed to provide a premium and personalized listening experience. This document provides a detailed overview of the app's architecture, features, and UI/UX design as it currently stands.

## Features

This version of Roshan Beats provides a solid and functional foundation with the following core features implemented:

*   **Dynamic Music Discovery:** Fetches real music data (tracks, albums, artists) from the Jamendo API.
*   **Core Music Playback:** A robust music player powered by ExoPlayer, supporting background playback with a `MediaSessionService`.
*   **AI-Powered Recommendations:** Integration with the Google Gemini API to provide personalized song recommendations based on user prompts.
*   **Local Playlist Management:** Users can create and manage their own playlists, which are stored locally using a Room database.
*   **Modern Android Architecture:** Built with a modern tech stack, including Kotlin, Jetpack Compose, MVVM, and Coroutines.
*   **Secure API Key Management:** API keys are handled securely and are not checked into version control.

## Technology Stack

*   **UI:** Jetpack Compose
*   **Architecture:** MVVM (Model-View-ViewModel)
*   **Asynchronous Programming:** Kotlin Coroutines
*   **Networking:** Retrofit & OkHttp
*   **Database:** Room Persistence Library
*   **Media Playback:** ExoPlayer (Media3)
*   **AI Integration:** Google Gemini API
*   **Dependency Injection:** Manual (via `ViewModelProvider.Factory`)

## Screen-by-Screen Breakdown

### 1. Splash Screen & Onboarding

*   **UI/UX:** The app launches with a simple, animated splash screen that fades in the app's name, "Roshan Beats," against a gradient background. This provides a professional and engaging entry point. After the splash screen, a one-time onboarding flow introduces new users to the app's key features with a series of swipeable screens.
*   **Logic:** The splash screen is displayed for a few seconds using a coroutine delay. The onboarding flow is built with the Accompanist Pager library and is shown only on the first launch.

### 2. Main Screen & Navigation

*   **UI/UX:** The main screen features a persistent bottom navigation bar with four main destinations: Home, Search, Library, and Profile. This provides consistent and intuitive navigation throughout the app.
*   **Logic:** The navigation is handled by Jetpack Navigation for Compose. A `NavHost` manages the different screens, and the `BottomNavigationBar` updates its state based on the current destination.

### 3. Home Screen

*   **UI/UX:** The home screen is designed for music discovery. It features horizontally scrolling lists for "Trending Now" albums and albums categorized by genre. It also includes a button to trigger AI-powered recommendations.
*   **Logic:** The `HomeViewModel` fetches data from the Jamendo API via the `MusicRepository`. Trending albums are fetched based on weekly popularity. The AI recommendation feature uses the Gemini API to get song suggestions, which are then searched for on Jamendo.

### 4. Search Screen

*   **UI/UX:** The search screen provides a powerful search experience. It includes a search bar and a tabbed layout to display results for tracks, albums, and artists separately.
*   **Logic:** The `SearchViewModel` uses the `MusicRepository` to query the Jamendo API for tracks, artists, and albums based on the user's search query.

### 5. "Now Playing" Screen (Mini & Full)

*   **UI/UX:** The "Now Playing" screen has two states:
    *   **Mini-Player:** A persistent mini-player at the bottom of the screen shows the currently playing track, its artist, album art, and a play/pause button.
    *   **Full-Screen Player:** Tapping the mini-player expands it to a full-screen, immersive experience with larger album art, full playback controls (play/pause, next, previous), and more details about the track.
*   **Logic:** This screen is implemented using a `BottomSheetScaffold`. The `NowPlayingViewModel` connects to the `MusicService` to get real-time playback state updates and to send playback commands.

### 6. Library Screen

*   **UI/UX:** The library screen is organized with tabs for Playlists, Albums, Artists, and Downloads. The "Playlists" tab displays a list of the user's custom playlists and includes a button to create new ones via a dialog.
*   **Logic:** The `LibraryViewModel` uses the `MusicRepository` to interact with the local Room database. Users can create new playlists, which are then persisted on the device.

### 7. Profile & Settings Screen

*   **UI/UX:** This screen displays the user's profile information (avatar, username, subscription tier) in a `Card` layout. Below the profile card, a list of settings options is displayed with corresponding icons.
*   **Logic:** The `ProfileViewModel` provides the user's profile data. The settings options are placeholders for now and will be implemented in a future version.

## Core Components

*   **MVVM Architecture:** The app follows the MVVM pattern, with each screen having its own ViewModel responsible for managing its state and business logic.
*   **ViewModelFactory:** A custom `ViewModelProvider.Factory` is used to correctly instantiate ViewModels that require an `Application` context, ensuring a stable and crash-free experience.
*   **Repository Pattern:** A `MusicRepository` is used to abstract the data sources (Jamendo API, Gemini API, Room database) from the ViewModels.
*   **MusicService:** A background `MediaSessionService` manages the ExoPlayer instance, allowing for background playback and integration with the Android media system.

## Setup Instructions

1.  **Clone the repository.**
2.  **Create `secrets.properties` file:** Before building the app, you need to create a `secrets.properties` file in the root directory of the project and add your API keys:
    ```properties
    # Gemini API Key
    GEMINI_API_KEY="YOUR_GEMINI_API_KEY"

    # OpenRouter API Keys
    DEEPSEEK_V3_1_API_KEY="YOUR_OPENROUTER_API_KEY"
    # ... other OpenRouter keys ...

    # Jamendo API Keys
    JAMENDO_CLIENT_ID="YOUR_JAMENDO_CLIENT_ID"
    JAMENDO_CLIENT_SECRET="YOUR_JAMENDO_CLIENT_SECRET"
    ```
3.  **Build and run the app.**
