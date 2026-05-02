package com.example.nammaplatform.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nammaplatform.data.Train
import com.example.nammaplatform.databinding.ItemTrainBinding

/**
 * Adapter for displaying a list of trains.
 * Optimized with DiffUtil and non-inner ViewHolder.
 */
class TrainAdapter(
    private var trains: List<Train> = emptyList(),
    private val onTrainSelected: (Train) -> Unit
) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    class TrainViewHolder(
        private val binding: ItemTrainBinding,
        private val onTrainSelected: (Train) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(train: Train) {
            binding.apply {
                tvTrainNumber.text = train.trainNumber
                tvTrainName.text = train.trainName
                tvPlatformNum.text = train.platform
                tvDepartureTime.text = train.departureTime

                // Display coach codes
                val coachCodes = train.coaches.map { it.displayName.firstOrNull()?.toString() ?: "" }
                    .joinToString(" ")
                tvCoachCodes.text = coachCodes

                root.setOnClickListener {
                    onTrainSelected(train)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val binding = ItemTrainBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrainViewHolder(binding, onTrainSelected)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        holder.bind(trains[position])
    }

    override fun getItemCount() = trains.size

    /**
     * Updates the train list using DiffUtil for efficient UI updates instead of notifyDataSetChanged.
     */
    fun updateTrains(newTrains: List<Train>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = trains.size
            override fun getNewListSize(): Int = newTrains.size

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return trains[oldItemPosition].trainNumber == newTrains[newItemPosition].trainNumber
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return trains[oldItemPosition] == newTrains[newItemPosition]
            }
        }

        val diffResult = DiffUtil.calculateDiff(diffCallback)
        trains = newTrains
        diffResult.dispatchUpdatesTo(this)
    }
}
