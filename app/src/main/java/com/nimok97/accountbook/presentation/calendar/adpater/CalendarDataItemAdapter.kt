package com.nimok97.accountbook.presentation.calendar.adpater

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nimok97.accountbook.databinding.ItemCalendarBinding
import com.nimok97.accountbook.domain.model.CalendarData

class CalendarDataItemAdapter :
    ListAdapter<CalendarData, CalendarDataItemAdapter.CalendarDateViewHolder>(
        CalendarDataItemDiffUtil
    ) {

    inner class CalendarDateViewHolder(val binding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(calendarData: CalendarData) {
            binding.calendarData = calendarData
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
        override fun areItemsTheSame(oldItem: CalendarData, newItem: CalendarData) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: CalendarData, newItem: CalendarData) =
            oldItem == newItem
    }
}