package com.nimok97.accountbook.presentation.history.adpater

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.nimok97.accountbook.R
import com.nimok97.accountbook.common.HISTORY_CONTENT
import com.nimok97.accountbook.common.HISTORY_HEADER
import com.nimok97.accountbook.common.printLog
import com.nimok97.accountbook.databinding.ItemHistoryContentBinding
import com.nimok97.accountbook.databinding.ItemHistoryFooterBinding
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
            HISTORY_HEADER -> 1
            HISTORY_CONTENT -> 2
            else -> 3
        }
    }

    inner class HeaderViewHolder(val binding: ItemHistoryHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(historyItem: HistoryItem) {
            binding.history = historyItem.history
            binding.tvAmountIncome.text = historyItem.income.toString()
            binding.tvAmountExpenditure.text = historyItem.expenditure.toString()
        }
    }

    inner class FooterViewHolder(val binding: ItemHistoryFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {}
    }

    inner class ContentViewHolder(val binding: ItemHistoryContentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            historyItem: HistoryItem,
            contentClick: (history: History) -> Unit,
            contentLongClick: (pos: Int, id: Int) -> Unit,
            checkContent: (id: Int) -> Unit
        ) {
            binding.history = historyItem.history
            binding.category = historyItem.category
            binding.method = historyItem.method
            historyItem.category?.let {
                binding.tvCategory.setBackgroundColor(Color.parseColor(it.color))
            }

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
                if (it) binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.white
                    )
                )
                else binding.root.setBackgroundColor(
                    ContextCompat.getColor(
                        binding.root.context,
                        R.color.primary_off_white
                    )
                )
            }

            historyItem.isChecked?.let {
                binding.cbDelete.isChecked = it
            }

            binding.root.setOnClickListener {
                historyItem.isLongClickMode?.let {
                    if (it) { // ????????? ??????
                        getItem(adapterPosition).isCheckVisible?.let {
                            if (it) { // ?????? -> ?????????
                                getItem(adapterPosition).isCheckVisible = false
                                getItem(adapterPosition).isChecked = false
                                binding.cbDelete.isVisible = false
                                binding.root.setBackgroundColor(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        R.color.primary_off_white
                                    )
                                )
                            } else {
                                getItem(adapterPosition).isCheckVisible = true
                                getItem(adapterPosition).isChecked = true
                                binding.cbDelete.isVisible = true
                                binding.cbDelete.isChecked = true
                                binding.root.setBackgroundColor(
                                    ContextCompat.getColor(
                                        binding.root.context,
                                        R.color.white
                                    )
                                )
                            }
                            checkContent.invoke(historyItem.history!!.id) // ????????? ?????? id ??????
                        }
                    } else { // ????????? ?????? ??????
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
            2 -> {
                val binding =
                    ItemHistoryContentBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return ContentViewHolder(binding)
            }
            else -> {
                val binding =
                    ItemHistoryFooterBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                return FooterViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(getItem(position))
            }
            is ContentViewHolder -> {
                holder.bind(getItem(position), contentClick, contentLongClick, checkContent)
            }
            is FooterViewHolder -> {
                holder.bind()
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

    fun disableLongClickMode() {
        val newList = ArrayList<HistoryItem>()
        currentList.forEachIndexed { index, historyItem ->
            newList.add(
                HistoryItem(
                    viewType = historyItem.viewType,
                    history = (if (historyItem.history != null) historyItem.history!!.copy() else null),
                    category = (if (historyItem.category != null) historyItem.category!!.copy() else null),
                    method = (if (historyItem.method != null) historyItem.method!!.copy() else null),
                    isLastItem = historyItem.isLastItem,
                    isCheckVisible = false,
                    isChecked = false,
                    income = historyItem.income,
                    expenditure = historyItem.expenditure,
                    isLongClickMode = false
                )
            )
        }
        submitList(newList.toList())
    }


    companion object HistoryItemDiffUtil : DiffUtil.ItemCallback<HistoryItem>() {

        override fun areItemsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
            (oldItem.history!!.id == newItem.history!!.id && oldItem.viewType == newItem.viewType)

        override fun areContentsTheSame(oldItem: HistoryItem, newItem: HistoryItem) =
            oldItem == newItem

    }
}