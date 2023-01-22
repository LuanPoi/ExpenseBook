package com.example.expensebook.ui.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.expensebook.R
import com.example.expensebook.databinding.DailyExpenseInfoItemBinding
import com.example.expensebook.databinding.ExpenseItemBinding
import com.example.expensebook.databinding.TitleItemBinding
import com.example.expensebook.data.model.EnumItemViewType
import com.example.expensebook.data.model.entity.Entry
import com.example.expensebook.data.model.entity.MonthlyExpense
import java.time.format.DateTimeFormatter

class HomeRecyclerViewListAdapter(private val viewModel: HomeViewModel): RecyclerView.Adapter<HomeRecyclerViewListAdapter.AbstractViewHolder>() {

    private var mainRecyclerViewArray = arrayListOf<Pair<EnumItemViewType, Any>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val abstractViewHolder: AbstractViewHolder
        abstractViewHolder = when(viewType){
            EnumItemViewType.TITLE.ordinal -> TitleItemViewHolder(TitleItemBinding.inflate(layoutInflater, parent, false), viewModel)
            EnumItemViewType.EXPENSE_ITEM.ordinal -> ExpenseItemViewHolder(ExpenseItemBinding.inflate(layoutInflater, parent, false), viewModel)
            EnumItemViewType.DAY_EXPENSE_CONTAINER.ordinal -> DailyExpenseInfoItemViewHolder(DailyExpenseInfoItemBinding.inflate(layoutInflater, parent, false), viewModel)
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

//    **********************************************************************************************
//    VIEW HOLDER
//    **********************************************************************************************
    abstract class AbstractViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract fun bind(obj: Pair<EnumItemViewType, Any>)
    }

    class TitleItemViewHolder(val binding: TitleItemBinding, val viewModel: HomeViewModel): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, title) = obj as Pair<EnumItemViewType, String>

            binding.textViewTitle.text = title
        }
    }

    class ExpenseItemViewHolder(val binding: ExpenseItemBinding, val viewModel: HomeViewModel): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, entry) = obj as Pair<EnumItemViewType, Entry>

            binding.textViewExpenseTitle.text = entry.description

            binding.textViewExpenseValue.text = "%.2f".format(entry.value)
            binding.textViewExpenseValue.setTextColor(ContextCompat.getColor(binding.root.context, if (entry.value >= 0) R.color.green_theme else R.color.pink_theme))

            binding.textViewExpenseDate.text = entry.date.format(DateTimeFormatter.ofPattern("dd/MM"))

            binding.containerExpenseItem.setOnLongClickListener {
                viewModel.deleteEntry(entry)
                true
            }
        }
    }

    class DailyExpenseInfoItemViewHolder(val binding: DailyExpenseInfoItemBinding, val viewModel: HomeViewModel): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, monthlyExpense) = obj as Pair<EnumItemViewType, MonthlyExpense>

            binding.textViewRecommendedAmountValue.text = "R$ %.2f".format(monthlyExpense.initial_value)+"*"
            binding.textViewExpendedAmountValue.text = "R$ %.2f".format(monthlyExpense.fixed_expense)+"*"
            binding.textViewRemainingAmountValue.text = "R$ %.2f".format(monthlyExpense.fixed_income)+"*"
        }

    }
}