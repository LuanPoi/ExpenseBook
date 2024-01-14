package com.thepoi.expensebook.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.thepoi.expensebook.R
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.databinding.DailyInfoItemBinding
import com.thepoi.expensebook.databinding.ExpenseItemBinding
import com.thepoi.expensebook.databinding.MonthlyInfoItemBinding
import com.thepoi.expensebook.databinding.TitleItemBinding
import com.thepoi.expensebook.ui.fragments.MonthlyExpenseDetails

class HomeRecyclerViewListAdapter(private val activity: FragmentActivity, private val viewModel: HomeViewModel): RecyclerView.Adapter<HomeRecyclerViewListAdapter.AbstractViewHolder>() {

    private var mainRecyclerViewArray = arrayListOf<Pair<EnumItemViewType, Any>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val abstractViewHolder: AbstractViewHolder = when(viewType){
            EnumItemViewType.TITLE.ordinal -> TitleItemViewHolder(TitleItemBinding.inflate(layoutInflater, parent, false))
            EnumItemViewType.EXPENSE_ITEM.ordinal -> ExpenseItemViewHolder(ExpenseItemBinding.inflate(layoutInflater, parent, false), viewModel)
            EnumItemViewType.DAY_EXPENSE_CONTAINER.ordinal -> DailyExpenseInfoItemViewHolder(
                DailyInfoItemBinding.inflate(layoutInflater, parent, false), viewModel)
            EnumItemViewType.MONTH_EXPENSE_CONTAINER.ordinal -> MonthlyExpenseInfoItemViewHolder(activity, MonthlyInfoItemBinding.inflate(layoutInflater, parent, false), viewModel)
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

    class TitleItemViewHolder(
        private val binding: TitleItemBinding
    ): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, title) = obj as Pair<EnumItemViewType, String>

            binding.textViewTitle.text = title
        }
    }

    class ExpenseItemViewHolder(
        private val binding: ExpenseItemBinding,
        private val viewModel: HomeViewModel
    ): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, uiState) = obj as Pair<EnumItemViewType, HomeUiState.EntryUiState>

            uiState.run {
                binding.itemIcon.setImageResource(binding.root.resources.getIdentifier(icon, "drawable", binding.root.context.packageName))
                binding.textViewExpenseTitle.text = description
                if(binding.textViewExpenseTitle.text.isNullOrBlank()){
                    binding.textViewExpenseTitle.visibility = View.GONE
                } else {
                    binding.textViewExpenseTitle.visibility = View.VISIBLE
                }
                binding.textViewExpenseValue.text = value.let { if (it.replace("[-+R$ ,.]".toRegex(), "").length >= 6) {
                    it.substring(0, it.length - 6) + "." + it.substring(it.length - 6)
                } else { it }}
                binding.textViewExpenseValue.setTextColor(ContextCompat.getColor(binding.root.context, if (value[0] == '-') R.color.custom_pink else R.color.custom_green))
                binding.textViewExpenseDate.text = date
            }

            binding.containerExpenseItem.setOnLongClickListener {
                viewModel.deleteEntry(Entry(id = uiState.id))
                true
            }

            binding.containerExpenseItem.setOnClickListener {
                val action = HomeFragmentDirections.actionHomeFragmentToEntryDetailsFragment(uiState.id.toString())
                findNavController(binding.root).navigate(action)
            }
        }
    }

    class DailyExpenseInfoItemViewHolder(
        private val binding: DailyInfoItemBinding,
        private val viewModel: HomeViewModel
    ): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, uiState) = obj as Pair<EnumItemViewType, HomeUiState.DayDataUiState>

            uiState.run {
                binding.textViewRecommendedAmountValue.text = recommendedDailyExpense
                binding.textViewExpendedAmountValue.text = expendToday
                binding.textViewRemainingAmountValue.text = remainingToday
            }
        }

    }

    class MonthlyExpenseInfoItemViewHolder(
        private val activity: FragmentActivity,
        private val binding: MonthlyInfoItemBinding,
        private val viewModel: HomeViewModel
    ): AbstractViewHolder(binding.root) {
        override fun bind(obj: Pair<EnumItemViewType, Any>) {
            val (enumViewType, uiState) = obj as Pair<EnumItemViewType, HomeUiState.MonthDataUiState>

            uiState.run {
                binding.imageButtonNextMonth.visibility = if (uiState.idOfNextMonthWithData != null) View.VISIBLE else View.INVISIBLE
                binding.imageButtonPreviousMonth.visibility = if (uiState.idOfPreviousMonthWithData != null) View.VISIBLE else View.INVISIBLE

                binding.textViewMonthName.text = binding.root.resources.getStringArray(R.array.months).get(monthNameOrdinal)
                binding.textViewProgressExpendValue.text = expend
                binding.textViewProgressRemainingValue.text = remaining
                binding.progressBarMonthBalance.progress = percentageExpend

                binding.textViewDetailsInitialValueValue.text = "R$ %.2f".format(initialValue)
                binding.textViewDetailsSavingsGoalValue.text = "R$ %.2f".format(savingsGoal)
            }

            binding.imageButtonPreviousMonth.setOnClickListener {
                uiState.idOfPreviousMonthWithData?.let {
                    viewModel.setSelectedMonth(it)
                }
            }

            binding.imageButtonNextMonth.setOnClickListener {
                uiState.idOfNextMonthWithData?.let {
                    viewModel.setSelectedMonth(it)
                }
            }

            binding.buttonDetailsManage.setOnClickListener {
                uiState.run {
                    val dialogFragment = MonthlyExpenseDetails(
                        initialValue,
                        savingsGoal
                    ) { newInitialValue, newSavingsGoal ->
                        Log.d("Dialog", "newInitialValue: $newInitialValue\nnewSavingsGoal: $newSavingsGoal")
                    }
                    dialogFragment.show(activity.supportFragmentManager, "MonthlyExpenseDetailsDialog")
                }
            }
        }

    }
}