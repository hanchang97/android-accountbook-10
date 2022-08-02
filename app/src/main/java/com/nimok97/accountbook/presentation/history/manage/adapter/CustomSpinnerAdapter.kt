package com.nimok97.accountbook.presentation.history.manage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.nimok97.accountbook.R
import com.nimok97.accountbook.databinding.ItemHistorySpinnerBinding
import com.nimok97.accountbook.databinding.SpinnerViewBinding
import com.nimok97.accountbook.domain.model.Method

class CustomSpinnerAdapter(
    val context: Context,
    val items: List<Method>
) : BaseAdapter() {
    override fun getCount() = items.size - 1

    override fun getItem(position: Int) = items[position]

    override fun getItemId(position: Int) = items[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.spinner_view, parent, false)
        val binding = SpinnerViewBinding.bind(view)
        //binding.tvContent.text = getItem(position).content
        if (position < items.lastIndex){
            binding.tvContent.setTextColor(ContextCompat.getColor(view.context, R.color.primary_purple))
        }
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view =
            LayoutInflater.from(context).inflate(R.layout.item_history_spinner, parent, false)
        val binding = ItemHistorySpinnerBinding.bind(view)
        binding.tvItem.text = items[position].content
        if (position == items.lastIndex - 1) {
            binding.ivAdd.isVisible = true
        }
        return binding.root
    }
}