package com.example.expensebook.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.expensebook.R
import com.example.expensebook.databinding.ExpenseItemBinding
import com.example.expensebook.model.entity.Entry
import java.time.format.DateTimeFormatter

class ExpenseHistoryListAdapter: RecyclerView.Adapter<ExpenseHistoryListAdapter.ViewHolder>() {

    var expenseHistoryList: List<Entry> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ExpenseItemBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(expenseHistoryList[position])
    }

    override fun getItemCount(): Int {
        return expenseHistoryList.size
    }

    fun setData(entryList: List<Entry>){
        this.expenseHistoryList = entryList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ExpenseItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(expense: Entry) {
            binding.textViewExpenseTitle.text = expense.description
            binding.textViewExpenseValue.text = expense.value.toString()
            if(expense.value > 0){
                binding.textViewExpenseValue.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_theme))
            } else {
                binding.textViewExpenseValue.setTextColor(ContextCompat.getColor(binding.root.context, R.color.pink_theme))
            }
            binding.textViewExpenseDate.text = expense.date.format(DateTimeFormatter.ofPattern("dd/MM"))
        }
    }
}