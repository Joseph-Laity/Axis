# Axis, an Apex Legends Stat Tracking App

## Overview

The Apex Legends Stat Tracker is an Android application that allows users to search for an Apex Legends player by username and view detailed gameplay statistics. The app retrieves real-time data from an external API and presents key performance metrics in a structured and readable format.

## Purpose

The purpose of this application is to provide users with an easy way to access and analyze Apex Legends player performance data, including kills, wins, kill/death ratio, win rate, damage, and rank.

---

## Key Features

- Search for Apex Legends players by username
- Display detailed player statistics
- Show kills, wins, K/D ratio, win rate, damage, and rank
- Real-time data retrieval from external API
- Error handling for invalid input, network issues, and missing data
- Clean and responsive Android UI
- MVVM architecture for scalable and maintainable design

---

## Tech Stack

### Architecture
- MVVM (Model-View-ViewModel)
- Repository Pattern
- LiveData
- ViewModel

### Technologies
- Kotlin
- Android SDK
- Retrofit (network requests)
- Kotlin Coroutines (asynchronous processing)
- ViewBinding
- Gson / JSON parsing library

---

## Project Structure
├── ui
│ ├── MainActivity.kt
│ ├── StatsActivity.kt
│
├── viewmodel
│ ├── PlayerViewModel.kt
│ ├── PlayerViewModelFactory.kt
│
├── repository
│ ├── PlayerRepository.kt
│
├── model
│ ├── Player.kt
│
├── network
│ ├── RetrofitInstance.kt
│ ├── NetworkResult.kt

---

## Installation Instructions

### Prerequisites
- Android Studio installed
- Minimum SDK configured in project
- Internet connection for API access

### Steps

1. Clone the repository
   
2. Open the project in Android Studio

3. Allow Gradle to sync and download dependencies

4. Add API key
  
5. Run the application on an emulator or physical device

---

## Usage Instructions

1. Launch the application
2. Enter a valid Apex Legends username
3. Press the search button
4. View the player's statistics on the results screen

---

## Example Behavior

### Valid Input
- Username entered correctly
- Player statistics are displayed successfully

### Invalid Input
- Empty username triggers error message
- Nonexistent username returns "Player not found"
- No internet connection displays network error message

---

## Troubleshooting

### App shows "No username provided"
Ensure a valid username is entered before searching.

### Player not found error
Check that the username is correct and exists in the API database.

### Network error
- Verify internet connection
- Ensure API service is available

### App crashes on launch
- Sync Gradle dependencies
- Check Logcat for missing or incorrect configurations

---

## Architecture Overview

This application uses the MVVM architecture:

- **View (Activities):** Handles UI and user interactions
- **ViewModel:** Manages UI-related data and business logic
- **Repository:** Handles data operations and API calls
- **Network Layer:** Retrofit handles HTTP requests

### Design Benefits

- Separation of concerns
- Easier testing and debugging
- Lifecycle-aware components
- Scalable and maintainable structure

---

## Third-Party Libraries

- Retrofit: API communication
- Gson: JSON parsing
- Kotlin Coroutines: asynchronous operations
- AndroidX Lifecycle Components: ViewModel and LiveData
- ViewBinding: type-safe UI access

---

## Contribution Guidelines

1. Fork the repository
2. Create a feature branch
3. Commit changes
4. Push to branch
5. Submit a pull request

---

## Contact

Developers: Joseph Laity - jbl265@nau.edu
            John Dastrup - jcd288@nau.edu
GitHub: https://github.com/Joseph-Laity/Axis
