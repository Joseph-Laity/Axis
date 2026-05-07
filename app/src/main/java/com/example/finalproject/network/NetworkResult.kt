package com.example.finalproject.network

/**
 * Represents the possible states of a network request.
 *
 * A sealed class is used so all possible response states
 * are known at compile time. This improves safety when
 * handling API responses inside the ViewModel and UI layer.
 *
 * Common usage:
 * - Loading -> show loading indicator
 * - Success -> display retrieved data
 * - Failure -> show error message
 */
sealed class NetworkResult<out T> {

    /**
     * Represents a successful API request.
     *
     * Contains the retrieved data from the server.
     */
    data class Success<out T>(val data: T) : NetworkResult<T>()

    /**
     * Represents a failed API request.
     *
     * Stores the thrown exception so detailed error
     * information can be logged or displayed.
     */
    data class Failure(val exception: Throwable) : NetworkResult<Nothing>()

    /**
     * Represents a request currently in progress.
     *
     * Used by the UI to display loading indicators
     * while waiting for the API response.
     */
    object Loading : NetworkResult<Nothing>()
}