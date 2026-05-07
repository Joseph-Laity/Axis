package com.example.finalproject.viewmodel

import androidx.lifecycle.*
import com.example.finalproject.model.Player
import com.example.finalproject.network.NetworkResult
import com.example.finalproject.repository.PlayerRepository
import kotlinx.coroutines.launch

/**
 * PlayerViewModel
 *
 * Purpose:
 * - Acts as the bridge between UI (Activity) and data layer (Repository)
 * - Handles asynchronous fetching of player statistics
 * - Exposes UI-friendly state (LiveData) for observation
 *
 * Architecture:
 * - Part of MVVM pattern
 * - Uses LiveData for reactive UI updates
 * - Uses coroutines (viewModelScope) for lifecycle-aware async operations
 *
 * Design decisions:
 * - Repository is injected via constructor for testability and separation of concerns
 * - UI does not directly handle network logic or error parsing
 * - Sealed class NetworkResult is used to standardize API responses
 */
class PlayerViewModel(
    private val repository: PlayerRepository
) : ViewModel() {

    /**
     * Holds successful player data.
     * Mutable internally, exposed as immutable LiveData to UI.
     */
    private val _player = MutableLiveData<Player>()
    val player: LiveData<Player> = _player

    /**
     * Holds error messages to be displayed in UI.
     */
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    /**
     * Tracks loading state for UI (e.g., show/hide progress indicator).
     */
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    /**
     * Fetches player data based on username.
     *
     * Flow:
     * 1. Set loading state to true
     * 2. Call repository to fetch data
     * 3. Set loading state to false
     * 4. Handle result (Success / Failure / Loading)
     *
     * Design note:
     * - Uses viewModelScope so coroutine is automatically cancelled
     *   when ViewModel is cleared (prevents memory leaks)
     */
    fun fetchPlayer(username: String) {
        viewModelScope.launch {

            // Indicate loading started
            _isLoading.postValue(true)

            // Request data from repository layer
            val result = repository.getPlayer(username)

            // Loading complete regardless of outcome
            _isLoading.postValue(false)

            /**
             * Handle sealed class result types.
             *
             * Design decision:
             * - Using sealed class ensures exhaustive when-statement handling
             * - Forces developer to explicitly handle Success/Failure/Loading cases
             */
            when (result) {

                // Successful API response
                is NetworkResult.Success -> {
                    _player.postValue(result.data)
                }

                // Failure cases from repository/network layer
                is NetworkResult.Failure -> {

                    /**
                     * Maps low-level technical errors into user-friendly messages.
                     *
                     * Design decision:
                     * - Keeps UI free of technical/network implementation details
                     * - Centralizes error interpretation in ViewModel layer
                     */
                    val msg = when (result.exception.message) {
                        "NETWORK" -> "No internet connection"
                        "HTTP_401" -> "Invalid API key"
                        "HTTP_403" -> "API access forbidden"
                        "HTTP_404" -> "Player not found"
                        "NO_DATA" -> "Player exists but no stats returned"
                        else -> "Unknown API error"
                    }

                    _error.postValue(msg)
                }

                // Optional loading state from repository (defensive handling)
                is NetworkResult.Loading -> {
                    _isLoading.postValue(true)
                }
            }
        }
    }
}