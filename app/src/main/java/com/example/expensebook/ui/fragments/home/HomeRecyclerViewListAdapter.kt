package com.example.expensebook.ui.fragments.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.expensebook.R
import com.example.expensebook.databinding.DailyExpenseInfoItemBinding
import com.example.expensebook.databinding.ExpenseItemBinding
import com.example.expensebook.databinding.TitleItemBinding
import com.example.expensebook.domain.model.Entry
import java.time.LocalDate
import java.time.ZoneId
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
            val (enumViewType, uiState) = obj as Pair<EnumItemViewType, HomeUiState>

            val today = LocalDate.now(ZoneId.systemDefault())
            val endOfMonth = uiState.currentMonthlyExpense.date.plusMonths(1).atDay(1)
            val remainingDays = endOfMonth.toEpochDay() - today.toEpochDay()
            val currentAmount = uiState.currentMonthlyExpense.initial_value - uiState.currentMonthlyExpense.savings_goal + uiState.currentEntries.map { it.value }.sum()
            val recomendedAmount = currentAmount / remainingDays
            val todayAmountExpend = uiState.currentEntries.filter { it.date.toLocalDate().isEqual(LocalDate.now(ZoneId.systemDefault())) }.map { it.value }.sum()
            val todayAmountRemaining = recomendedAmount + todayAmountExpend

            Log.d(TAG, "current amount: $currentAmount\nremaining days: $remainingDays\nrecomended amount: $recomendedAmount\ntoday expend: $todayAmountExpend\ntoday remaining: $todayAmountRemaining\n")

            binding.textViewRecommendedAmountValue.text = "R$ %.2f".format(recomendedAmount)
            binding.textViewExpendedAmountValue.text = "R$ %.2f".format(if (todayAmountExpend.equals(0f)) todayAmountExpend else todayAmountExpend.times(-1))
            binding.textViewRemainingAmountValue.text = "R$ %.2f".format(todayAmountRemaining)
        }

    }

    companion object {
        const val TAG = "HomeRecyclerViewListAdapter"
    }
}