package com.example.nammaplatform.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nammaplatform.data.Coach
import com.example.nammaplatform.data.CoachType
import com.example.nammaplatform.databinding.ItemCoachBinding

/**
 * Adapter for displaying coach information in a horizontal list.
 * Optimized with DiffUtil and non-inner ViewHolder.
 */
class CoachAdapter(
    private var coaches: List<Coach> = emptyList()
) : RecyclerView.Adapter<CoachAdapter.CoachViewHolder>() {

    // Removed 'inner' modifier as it's not needed and can cause memory leaks
    class CoachViewHolder(private val binding: ItemCoachBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coach: Coach) {
            binding.apply {
                tvCoachName.text = coach.displayName
                tvCoachType.text = coach.coachType.displayName

                // Set background color based on coach type
                val color = ContextCompat.getColor(root.context, coach.coachType.colorRes)
                cvCoach.setCardBackgroundColor(color)

                // Highlight General coaches
                if (coach.coachType == CoachType.GENERAL) {
                    cvCoach.elevation = 8f
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoachViewHolder {
        val binding = ItemCoachBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CoachViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoachViewHolder, position: Int) {
        holder.bind(coaches[position])
    }

    override fun getItemCount() = coaches.size

    /**
     * Updates the coach list using DiffUtil for efficient UI updates.
     */
    fun updateCoaches(newCoaches: List<Coach>) {
        val diffCallback = object : DiffUtil.Callback() {
            override fun getOldListSize(): Int = coaches.size
            override fun getNewListSize(): Int = newCoaches.size
            
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return coaches[oldItemPosition].coachId == newCoaches[newItemPosition].coachId
            }
            
            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return coaches[oldItemPosition] == newCoaches[newItemPosition]
            }
        }
        
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        coaches = newCoaches
        diffResult.dispatchUpdatesTo(this)
    }
}
