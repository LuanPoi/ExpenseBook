package com.example.expensebook.ui.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.expensebook.R
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.databinding.DailyInfoItemBinding
import com.example.expensebook.databinding.ExpenseItemBinding
import com.example.expensebook.databinding.MonthlyInfoItemBinding
import com.example.expensebook.databinding.TitleItemBinding

class HomeRecyclerViewListAdapter(private val viewModel: HomeViewModel): RecyclerView.Adapter<HomeRecyclerViewListAdapter.AbstractViewHolder>() {

    private var mainRecyclerViewArray = arrayListOf<Pair<EnumItemViewType, Any>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val abstractViewHolder: AbstractViewHolder = when(viewType){
            EnumItemViewType.TITLE.ordinal -> TitleItemViewHolder(TitleItemBinding.inflate(layoutInflater, parent, false))
            EnumItemViewType.EXPENSE_ITEM.ordinal -> ExpenseItemViewHolder(ExpenseItemBinding.inflate(layoutInflater, parent, false), viewModel)
            EnumItemViewType.DAY_EXPENSE_CONTAINER.ordinal -> DailyExpenseInfoItemViewHolder(DailyInfoItemBinding.inflate(layoutInflater, parent, false))
            EnumItemViewType.MONTH_EXPENSE_CONTAINER.ordinal -> MonthlyExpenseInfoItemViewHolder(MonthlyInfoItemBinding.inflate(layoutInflater, parent, false))
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

    companion object {
        const val TAG = "HomeRecyclerViewListAdapter"
    }

//    **********************************************************************************************
//    VIEW HOLDER
//    **********************************************************************************************
    abstract class AbstractViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        abstract fun bind(obj: Pair<EnumItemViewType, Any>)
    }

    class TitleItemViewHolder(private val binding: TitleItemBinding): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, title) = obj as Pair<EnumItemViewType, String>

            binding.textViewTitle.text = title
        }
    }

    class ExpenseItemViewHolder(private val binding: ExpenseItemBinding, private val viewModel: HomeViewModel): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, uiState) = obj as Pair<EnumItemViewType, HomeUiState.EntryUiState>

            uiState.run {
                binding.itemIcon.setImageResource(binding.root.resources.getIdentifier(icon, "drawable", binding.root.context.packageName))
                binding.textViewExpenseTitle.text = description
                binding.textViewExpenseValue.text = value
                binding.textViewExpenseValue.setTextColor(ContextCompat.getColor(binding.root.context, if (value[0] == '-') R.color.pink_theme else R.color.green_theme))
                binding.textViewExpenseDate.text = date
            }

            binding.containerExpenseItem.setOnLongClickListener {
                viewModel.deleteEntry(Entry(uid = uiState.id))
                true
            }
        }
    }

    class DailyExpenseInfoItemViewHolder(private val binding: DailyInfoItemBinding): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, uiState) = obj as Pair<EnumItemViewType, HomeUiState.DayDataUiState>

            uiState.run {
                binding.textViewRecommendedAmountValue.text = recommendedDailyExpense
                binding.textViewExpendedAmountValue.text = expendToday
                binding.textViewRemainingAmountValue.text = remainingToday
            }
        }

    }

    class MonthlyExpenseInfoItemViewHolder(private val binding: MonthlyInfoItemBinding): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, uiState) = obj as Pair<EnumItemViewType, HomeUiState.MonthDataUiState>

            uiState.run {
                binding.textViewMonthName.text = binding.root.resources.getStringArray(R.array.months).get(monthNameOrdinal)
                binding.textViewProgressExpendValue.text = expend
                binding.textViewProgressRemainingValue.text = remaining
                binding.progressBarMonthBalance.progress = percentageExpend

                binding.textViewDetailsInitialValueValue.text = initialValue
                binding.textViewDetailsSavingsGoalValue.text = savingGoal
            }

            binding.buttonDetailsManage.setOnClickListener {
                findNavController(binding.root).navigate(R.id.action_homeFragment_to_onboardFragment)
            }
        }

    }
}