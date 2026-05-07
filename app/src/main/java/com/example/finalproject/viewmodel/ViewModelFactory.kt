package com.example.finalproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.finalproject.repository.PlayerRepository

/**
 * PlayerViewModelFactory
 *
 * Purpose:
 * - Responsible for creating instances of PlayerViewModel
 * - Required because PlayerViewModel has a non-empty constructor
 *
 * Why this exists (important design decision):
 * - ViewModelProvider normally expects a no-argument ViewModel constructor
 * - Since we need to pass a PlayerRepository dependency, we use a Factory
 * - This enables Dependency Injection without external frameworks (like Hilt)
 *
 * Benefits:
 * - Keeps ViewModel testable (can inject mock repository)
 * - Maintains separation of concerns
 * - Avoids tight coupling between ViewModel and data source creation
 */
class PlayerViewModelFactory(
    private val repository: PlayerRepository
) : ViewModelProvider.Factory {

    /**
     * Creates the ViewModel instance.
     *
     * Generic type handling:
     * - Android uses generics here to allow multiple ViewModel types
     * - We safely cast because we know we are only creating PlayerViewModel
     *
     * Design note:
     * - Suppressing unchecked cast is common in ViewModelFactory patterns
     * - Could be improved with modelClass.isAssignableFrom check in larger apps
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        // We explicitly construct PlayerViewModel with injected repository
        return PlayerViewModel(repository) as T
    }
}