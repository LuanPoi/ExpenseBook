package com.thepoi.expensebook.ui.fragments.entry_details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.thepoi.expensebook.R
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.databinding.FragmentEntryDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Long.parseLong
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.absoluteValue

@AndroidEntryPoint
class EntryDetailsFragment : DialogFragment() {

    private var entryId: Long? = null

    private lateinit var binding: FragmentEntryDetailsBinding

    private lateinit var entryDetailViewModel: EntryDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("entryId")?.let { entryId = parseLong(it) }
        entryDetailViewModel = ViewModelProvider(this)[EntryDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        dialog?.let {dialog ->
            dialog.window?.let {window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
                window.requestFeature(Window.FEATURE_NO_TITLE);
            }
        }
        binding = FragmentEntryDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entryDetailViewModel.stateOnceAndStream(entryId).observe(viewLifecycleOwner){ uiState ->
            bindUiState(uiState)
        }.also {
            binding.switchEntryType.setOnClickListener {
                entryDetailViewModel.stateOnceAndStream(null).value?.let {
                    val dateParsed = LocalDate.parse(binding.textViewDate.text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    entryDetailViewModel.updateValues(it.copy(
                        datetime = ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS).with(dateParsed),
                        isIncome = binding.switchEntryType.isChecked,
                        amount = binding.editTextValue.text?.toString()?.ifEmpty { null }?.replace(".", "")?.replace(",", ".")?.toFloat(),
                        description = binding.textInputDescription.editText?.text.toString()
                    ))
                }
            }
            binding.buttonCalendar.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date").setCalendarConstraints(CalendarConstraints.Builder().setValidator(
                            DateValidatorPointBackward.now()).build())
                        .build()
                parentFragmentManager.let {
                    datePicker.show(it, "teste")
                    datePicker.addOnPositiveButtonClickListener {
                        binding.textViewDate.text = LocalDateTime.from(Instant.ofEpochMilli(it).atOffset(
                            ZoneOffset.UTC)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    }
                }
            }
        }

        binding.buttonConfirm.setOnClickListener { onSave().also { dismiss() } }

        binding.buttonCancel.setOnClickListener { dismiss() }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun bindUiState(uiState: EntryDetailsUiState){
        binding.textViewDate.text = uiState.datetime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        binding.switchEntryType.isChecked = uiState.isIncome //true=income | false=expense
        uiState.amount?.let { binding.editTextValue.setText("%.2f".format(it.absoluteValue)) }
        uiState.description.let { binding.textInputDescription.editText?.setText(it) }
        if (uiState.isIncome){
            binding.textViewValuePrefix.text = "+ R$"
            binding.textViewValuePrefix.setTextColor(ContextCompat.getColor(binding.root.context, R.color.custom_green))
            binding.editTextValue.setTextColor(ContextCompat.getColor(binding.root.context, R.color.custom_green))
            binding.textViewSwitchIncome.setTextColor(ContextCompat.getColor(binding.root.context, R.color.custom_green))
            binding.textViewSwitchExpense.setTextColor(ContextCompat.getColor(binding.root.context, R.color.purple_900))
        } else {
            binding.textViewValuePrefix.text = "- R$"
            binding.textViewValuePrefix.setTextColor(ContextCompat.getColor(binding.root.context, R.color.custom_pink))
            binding.editTextValue.setTextColor(ContextCompat.getColor(binding.root.context, R.color.custom_pink))
            binding.textViewSwitchExpense.setTextColor(ContextCompat.getColor(binding.root.context, R.color.custom_pink))
            binding.textViewSwitchIncome.setTextColor(ContextCompat.getColor(binding.root.context, R.color.purple_900))
        }
    }

    fun onSave(){
        if(binding.editTextValue.text.toString().isEmpty()) return
        if(binding.editTextValue.text.toString()
            .replace(".", "")
            .replace(",", ".")
            .toFloat() == 0f) return
        val entry: Entry = Entry(
            entryId?.toLong(),
            ZonedDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS).with(LocalDate.parse(binding.textViewDate.text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
            if(binding.switchEntryType.isChecked) {
                binding.editTextValue.text.toString()
                    .replace(".", "")
                    .replace(",", ".")
                    .toFloat()
            } else {
                binding.editTextValue.text.toString()
                    .replace(".", "")
                    .replace(",", ".")
                    .toFloat().times(-1)
            },
            binding.textInputDescription.editText?.text.toString()
        )
        entryDetailViewModel.save(entry)
    }

    companion object {
        fun newInstance(entryId: Long?): EntryDetailsFragment {
            val fragment = EntryDetailsFragment()
            val args = Bundle()
            entryId?.let { args.putString("entryId", it.toString()) }
            fragment.arguments = args
            return fragment
        }
    }
}