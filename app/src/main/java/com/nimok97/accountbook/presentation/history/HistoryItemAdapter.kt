package com.nimok97.accountbook.presentation.history

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.ItemHistoryContentBinding
import com.nimok97.accountbook.databinding.ItemHistoryHeaderBinding
import com.nimok97.accountbook.domain.model.History
import com.nimok97.accountbook.domain.model.HistoryItem

class HistoryItemAdapter(
    private val contentClick: (history: History) -> Unit,
    private val contentLongClick: (pos: Int, id: Int) -> Unit,
    private val checkContent: (id: Int) -> Unit
) :
    ListAdapter<HistoryItem, RecyclerView.ViewHolder>(HistoryItemDiffUtil) {

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position).viewType) {
            "header" -> 1
            else -> 2
        }
    }

    class HeaderViewHolder(val binding: ItemHistoryHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryItem) {
            binding.history = historyItem.history
            binding.tvAmountIncome.text = historyItem.income.toString()
            binding.tvAmountExpenditure.text = historyItem.expenditure.toString()
        }
    }

    class ContentViewHolder(val binding: ItemHistoryContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            historyItem: HistoryItem,
            contentClick: (history: History) -> Unit,
            contentLongClick: (pos: Int, id: Int) -> Unit
        ) {
            binding.history = historyItem.history
            binding.category = historyItem.category
            binding.method = historyItem.method

            historyItem.isLastItem?.let {
                if (it) {
                    binding.viewDividerNotLast.isVisible = false
                    binding.viewDividerLast.isVisible = true
                } else {
                    binding.viewDividerNotLast.isVisible = true
                    binding.viewDividerLast.isVisible = false
                }
            }

            historyItem.isCheckVisible?.let {
                binding.cbDelete.isVisible = it
            }

            historyItem.isChecked?.let {
                binding.cbDelete.isChecked = it
            }
            
            binding.root.setOnClickListener {
                historyItem.isLongClickMode?.let {
                    if (!it) {
                        historyItem.isCheckVisible?.let {
                            if (!it) {
                                historyItem.history?.let {
                                    printLog("history content selected : $it")
                                    contentClick.invoke(it)
                                }
                            }
                        }
                    }
                }
            }

            binding.root.setOnLongClickListener(View.OnLongClickListener {
                historyItem.isLongClickMode?.let {
                    if (!it) {
                        contentLongClick.invoke(adapterPosition, historyItem.history!!.id)
                    }
                }
                return@OnLongClickListener true
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> {
                val binding =
                    ItemHistoryHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return HeaderViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemHistoryContentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ContentViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position))
            }
            is ContentViewHolder -> {
                holder.bind(getItem(position), contentClick, contentLongClick)
            }
        }
    }

    fun enableLongClickMode(pos: Int) {
        val newList = ArrayList<HistoryItem>()
        currentList.forEachIndexed { index, historyItem ->
            newList.add(
                HistoryItem(
                    viewType = historyItem.viewType,
                    history = (if (historyItem.history != null) historyItem.history!!.copy() else null),
                    category = (if (historyItem.category != null) historyItem.category!!.copy() else null),
                    method = (if (historyItem.method != null) historyItem.method!!.copy() else null),
                    isLastItem = historyItem.isLastItem,
                    isCheckVisible = (pos == index),
                    isChecked = (pos == index),
                    income = historyItem.income,
                    expenditure = historyItem.expenditure,
                    isLongClickMode = true
                )
            )
        }
        submitList(newList.toList())
    }


    companion object HistoryItemDiffUtil : DiffUtil.ItemCallback<HistoryItem>() {

        override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
            oldItem.history!!.id == oldItem.history!!.id

        override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
            oldItem == newItem

    }
}