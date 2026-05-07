package com.example.finalproject.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityMainBinding

/**
 * Main entry screen of the app.
 *
 * Responsibilities:
 * - Collect user input (username)
 * - Navigate to StatsActivity with the provided username
 *
 * Design decision:
 * - Uses ViewBinding instead of findViewById for type safety,
 *   null safety, and improved readability.
 * - No validation is performed here to keep UI lightweight;
 *   downstream activity is responsible for handling empty/invalid usernames.
 */
class MainActivity : AppCompatActivity() {

    // ViewBinding instance for accessing UI elements safely
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)

        // Set the root view of the binding as the content view
        setContentView(binding.root)

        /**
         * Button click listener that triggers navigation to StatsActivity.
         *
         * Flow:
         * 1. Read user input from EditText
         * 2. Create an explicit Intent to StatsActivity
         * 3. Attach username as an extra ("USERNAME")
         * 4. Start new activity
         *
         * Design note:
         * - Username is passed via Intent extras instead of global state
         *   to keep activities loosely coupled and lifecycle-safe.
         */
        binding.searchButton.setOnClickListener {

            // Retrieve text input from user
            val username = binding.usernameInput.text.toString()

            // Create intent to navigate to StatsActivity
            val intent = Intent(this, StatsActivity::class.java)

            // Pass username to next screen
            intent.putExtra("USERNAME", username)

            // Launch StatsActivity
            startActivity(intent)
        }
    }
}