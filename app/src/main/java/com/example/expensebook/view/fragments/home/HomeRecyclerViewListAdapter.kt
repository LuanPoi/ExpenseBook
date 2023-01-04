package com.example.expensebook.view.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.expensebook.R
import com.example.expensebook.databinding.ExpenseItemBinding
import com.example.expensebook.databinding.TitleItemBinding
import com.example.expensebook.model.EnumItemViewType
import com.example.expensebook.model.entity.Entry
import java.time.format.DateTimeFormatter

class HomeRecyclerViewListAdapter: RecyclerView.Adapter<HomeRecyclerViewListAdapter.AbstractViewHolder>() {

    private var mainRecyclerViewArray = arrayListOf<Pair<EnumItemViewType, Any>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val abstractViewHolder: AbstractViewHolder
        abstractViewHolder = when(viewType){
            EnumItemViewType.TITLE.ordinal -> TitleItemViewHolder(TitleItemBinding.inflate(layoutInflater, parent, false))
            EnumItemViewType.EXPENSE_ITEM.ordinal -> ExpenseItemViewHolder(ExpenseItemBinding.inflate(layoutInflater, parent, false))
            else -> throw NotImplementedError()
        }
        return abstractViewHolder
    }

    override fun getItemViewType(position: Int): Int {
        return mainRecyclerViewArray[position].first.ordinal
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        holder.bind(mainRecyclerViewArray[position])
    }

    override fun getItemCount(): Int {
        return mainRecyclerViewArray.size
    }

    fun setData(entryList: ArrayList<Pair<EnumItemViewType, Any>>){
        this.mainRecyclerViewArray = entryList
        notifyDataSetChanged()
    }

    abstract class AbstractViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract fun bind(obj: Pair<EnumItemViewType, Any>)
    }

    class TitleItemViewHolder(val binding: TitleItemBinding): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            var (enumViewType, title) = obj as Pair<EnumItemViewType, String>

            binding.textViewTitle.text = title
        }
    }

    class ExpenseItemViewHolder(val binding: ExpenseItemBinding): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            var (enumViewType, expense) = obj as Pair<EnumItemViewType, Entry>

            binding.textViewExpenseTitle.text = expense.description

            binding.textViewExpenseValue.text = expense.value.toString()
            binding.textViewExpenseValue.setTextColor(ContextCompat.getColor(binding.root.context, if (expense.value >= 0) R.color.green_theme else R.color.pink_theme))

            binding.textViewExpenseDate.text = expense.date.format(DateTimeFormatter.ofPattern("dd/MM"))

            binding.containerExpenseItem.setOnLongClickListener {
                Toast.makeText(it.context,"value ${expense.value}", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }
}