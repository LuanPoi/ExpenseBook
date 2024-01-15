package com.thepoi.expensebook.ui.fragments.entry_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import com.thepoi.expensebook.R
import com.thepoi.expensebook.data.data_source.local.entities.Entry
import com.thepoi.expensebook.databinding.FragmentEntryDetailsBinding
import dagger.hilt.android.AndroidEntryPoint
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
class EntryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEntryDetailsBinding

    private lateinit var entryDetailViewModel: EntryDetailsViewModel

    val args: EntryDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryDetailViewModel = ViewModelProvider(this)[EntryDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentEntryDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        entryDetailViewModel.stateOnceAndStream(args.entryId?.toLong()).observe(viewLifecycleOwner){ uiState ->
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

        binding.buttonConfirm.setOnClickListener { onSave().also { findNavController().navigateUp() } }
        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }
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
        val entry: Entry = Entry(
            args.entryId?.toLong(),
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
}