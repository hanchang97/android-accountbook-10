package com.nimok97.accountbook.presentation.calendar.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nimok97.accountbook.R
import com.nimok97.accountbook.databinding.ItemCalendarBinding
import com.nimok97.accountbook.domain.model.CalendarData

class CalendarDataItemAdapter(currentYear: Int, currentMonth: Int, currentDay: Int) :
    ListAdapter<CalendarData, CalendarDataItemAdapter.CalendarDateViewHolder>(
        CalendarDataItemDiffUtil
    ) {

    val currentYear = currentYear
    val currentMonth = currentMonth
    val currentDay = currentDay

    inner class CalendarDateViewHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarData: CalendarData) {
            binding.calendarData = calendarData
            if (calendarData.year == currentYear && calendarData.month == currentMonth && calendarData.day == currentDay) {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
            } else {
                binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.primary_off_white
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarDateViewHolder {
        val binding =
            ItemCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)

//        var params = binding.root.layoutParams
//        params.width = parent.measuredWidth / 7

        return CalendarDateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CalendarDateViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object CalendarDataItemDiffUtil : DiffUtil.ItemCallback<CalendarData>() {
        override fun areItemsTheSame(oldItem: CalendarData, newItem: CalendarData): Boolean {
            return if (oldItem.year == newItem.year && oldItem.month == newItem.month && oldItem.day == newItem.day) {
                (oldItem.year == newItem.year && oldItem.month == newItem.month && oldItem.day == newItem.day)
            } else {
                oldItem == newItem
            }
        }

        override fun areContentsTheSame(oldItem: CalendarData, newItem: CalendarData) =
            oldItem == newItem
    }
}