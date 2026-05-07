package com.example.finalproject.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.finalproject.databinding.ActivityStatsBinding
import com.example.finalproject.repository.PlayerRepository
import com.example.finalproject.viewmodel.PlayerViewModel
import com.example.finalproject.viewmodel.PlayerViewModelFactory

/**
 * StatsActivity
 *
 * Purpose:
 * - Displays player statistics fetched from a remote/local data source
 * - Receives a username from MainActivity and uses it to query player data
 *
 * Architecture:
 * - Uses MVVM (Model-View-ViewModel) pattern:
 *   - ViewModel handles data fetching and state
 *   - Repository abstracts data source logic
 *   - Activity only observes state and updates UI
 *
 * Design decisions:
 * - ViewBinding is used to avoid findViewById and reduce runtime errors
 * - LiveData observation ensures UI reacts automatically to data changes
 * - Factory pattern is used to inject repository dependency into ViewModel
 *   (keeps ViewModel testable and decoupled from concrete implementations)
 */
class StatsActivity : AppCompatActivity() {

    // ViewBinding instance for safe UI access
    private lateinit var binding: ActivityStatsBinding

    /**
     * ViewModel scoped to this Activity.
     *
     * Design note:
     * - viewModels delegate ensures ViewModel survives configuration changes
     * - Factory is required because PlayerViewModel has a non-empty constructor
     */
    private val viewModel: PlayerViewModel by viewModels {
        PlayerViewModelFactory(PlayerRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using ViewBinding
        binding = ActivityStatsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /**
         * Retrieve username passed from MainActivity.
         *
         * Design decision:
         * - Intent extras are used for simple, lightweight data transfer
         *   between activities instead of shared global state.
         */
        val username = intent.getStringExtra("USERNAME")

        /**
         * Input validation:
         * - If username is missing or empty, we cannot proceed with API call
         * - Show error feedback and close screen to avoid invalid state
         */
        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "No username provided", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Trigger data fetch through ViewModel (delegates to Repository)
        viewModel.fetchPlayer(username)

        /**
         * Observe player data updates.
         *
         * When data changes:
         * - UI is automatically updated
         * - Null check ensures we don’t crash on incomplete responses
         */
        viewModel.player.observe(this) { player ->

            if (player == null) return@observe

            // Bind API/model data to UI components
            binding.playerName.text = player.username
            binding.kdValue.text = player.kd
            binding.winsValue.text = player.wins
            binding.killsValue.text = player.kills
            binding.winRateValue.text = player.winRate
            binding.damageValue.text = player.damage
            binding.rankValue.text = player.rank
        }

        /**
         * Observe error state from ViewModel.
         *
         * Design decision:
         * - Errors are handled centrally in ViewModel rather than Activity
         * - Keeps UI layer thin and focused only on rendering
         */
        viewModel.error.observe(this) { msg ->
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        }
    }
}