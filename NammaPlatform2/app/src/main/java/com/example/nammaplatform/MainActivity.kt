package com.example.nammaplatform

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nammaplatform.data.Coach
import com.example.nammaplatform.data.Train
import com.example.nammaplatform.databinding.ActivityMainBinding
import com.example.nammaplatform.ui.adapter.CoachAdapter
import com.example.nammaplatform.ui.adapter.TrainAdapter
import com.example.nammaplatform.viewmodel.RailwayViewModel
import java.util.*

/**
 * MainActivity displays train information, coach positions, and handles announcements.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RailwayViewModel
    private var tts: TextToSpeech? = null
    private var currentTrain: Train? = null
    private var selectedStationIndex = 0

    private lateinit var coachAdapter: CoachAdapter
    private lateinit var trainAdapter: TrainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel using modern syntax
        viewModel = ViewModelProvider(this)[RailwayViewModel::class.java]

        // Initialize UI components and adapters
        setupAdapters()

        // Load data from the ViewModel
        loadTrainData()

        // Initialize Text-to-Speech engine
        initializeTTS()

        // Setup click listeners for interactive elements
        setupClickListeners()
    }

    private fun setupAdapters() {
        // Setup Coach RecyclerView (Horizontal)
        coachAdapter = CoachAdapter()
        binding.rvCoaches.apply {
            adapter = coachAdapter
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }

        // Setup Train RecyclerView (Vertical)
        trainAdapter = TrainAdapter { train ->
            displayTrainDetails(train)
        }
        binding.rvTrains.apply {
            adapter = trainAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun loadTrainData() {
        viewModel.loadTrainData(this)
        val nextTrains = viewModel.getNextTrains(3)

        // Display the first train by default
        currentTrain = viewModel.getNextTrain()
        currentTrain?.let { displayTrainDetails(it) }

        // Update the list of upcoming trains
        trainAdapter.updateTrains(nextTrains)
    }

    private fun displayTrainDetails(train: Train) {
        currentTrain = train

        binding.apply {
            tvTrainNumberMain.text = train.trainName
            // Optimized: Use formatted string resource instead of concatenation
            tvDepartureTimeMain.text = getString(R.string.departure_format, train.departureTime)
            tvPlatformNum.text = train.platform

            coachAdapter.updateCoaches(train.coaches)
            updatePositionIndicators(train.coaches)
        }
    }

    private fun updatePositionIndicators(coaches: List<Coach>) {
        binding.llPositionIndicators.removeAllViews()
        coaches.forEach { coach ->
            binding.llPositionIndicators.addView(createPositionIndicatorView(coach))
        }
    }

    private fun createPositionIndicatorView(coach: Coach): LinearLayout {
        // Dynamic view creation for coach position visualization
        val container = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(0, 8, 0, 8)
        }

        // Coach Label (e.g., "S1 (Sleeper)")
        val tvCoachName = TextView(this).apply {
            text = getString(R.string.coach_name_format, coach.displayName, coach.coachType.displayName)
            textSize = 10f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.black))
        }
        container.addView(tvCoachName)

        // Progress bar track
        val barContainer = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 48)
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 4, 0, 4)
        }

        // Background (track)
        val bgBar = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, 8, 1f)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
        }
        
        // Foreground (actual coach position)
        val posBar = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams((coach.position * 3), 8)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, coach.coachType.colorRes))
        }
        bgBar.addView(posBar)
        barContainer.addView(bgBar)

        // Percentage text
        val tvPosition = TextView(this).apply {
            text = getString(R.string.position_percent_format, coach.position)
            textSize = 8f
            setTextColor(ContextCompat.getColor(this@MainActivity, android.R.color.darker_gray))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { marginStart = 8 }
        }
        barContainer.addView(tvPosition)

        container.addView(barContainer)
        return container
    }

    private fun initializeTTS() {
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val locale = Locale.forLanguageTag("kn-IN")
                tts?.let { safeTts ->
                    val result = safeTts.setLanguage(locale)
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        safeTts.language = Locale.getDefault()
                    }
                }
            }
        }
    }

    private fun setupClickListeners() {
        // Speak announcement in Kannada
        binding.btnSpeak.setOnClickListener {
            currentTrain?.let { speakAnnouncement(it) }
        }

        // Change current station
        binding.llStationSelector.setOnClickListener {
            showStationSelector()
        }
    }

    private fun speakAnnouncement(train: Train) {
        val coachNames = train.coaches.joinToString(", ") { it.displayName }
        val coachText = if (coachNames.isNotEmpty()) {
            getString(R.string.coaches_label, coachNames)
        } else {
            ""
        }

        // Localized announcement format
        val text = getString(R.string.announcement_format, train.trainName, train.platform, coachText)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun showStationSelector() {
        val stations = viewModel.loadStations()
        val stationNames = stations.map { "${it.kannada} - ${it.stationName}" }

        AlertDialog.Builder(this)
            .setTitle(R.string.station_selector_title)
            .setSingleChoiceItems(stationNames.toTypedArray(), selectedStationIndex) { dialog, which ->
                selectedStationIndex = which
                val selectedStation = stations[which]
                binding.tvStation.text = selectedStation.kannada
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroy() {
        // Proper resource management for TTS
        tts?.apply {
            stop()
            shutdown()
        }
        super.onDestroy()
    }
}
