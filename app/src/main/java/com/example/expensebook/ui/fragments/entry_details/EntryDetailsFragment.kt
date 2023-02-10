package com.example.expensebook.ui.fragments.entry_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensebook.R
import com.example.expensebook.data.data_source.local.entities.Entry
import com.example.expensebook.databinding.FragmentEntryDetailsBinding
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class EntryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEntryDetailsBinding

    private lateinit var entryDetailViewModel: EntryDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryDetailViewModel = ViewModelProvider(this)[EntryDetailsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEntryDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        entryDetailViewModel.stateOnceAndStream().observe(viewLifecycleOwner){ uiState ->
            bindUiState(uiState)
        }.also {
            binding.switchEntryType.setOnClickListener {
                entryDetailViewModel.stateOnceAndStream().value?.let {
                    val dateParsed = LocalDate.parse(binding.textViewDate.text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    entryDetailViewModel.updateValues(it.copy(
                        date = OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS).with(dateParsed),
                        isIncome = binding.switchEntryType.isChecked,
                        value = binding.editTextValue.text.toString().toFloat(),
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
        binding.textViewDate.setText(uiState.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        binding.switchEntryType.isChecked = uiState.isIncome //true=receipt | false=expense
        binding.editTextValue.setText(uiState.value.toString())
        binding.textInputDescription.editText?.setText(uiState.description)
        if (uiState.isIncome){
            binding.textViewValuePrefix.text = "+ R$"
            binding.textViewValuePrefix.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_theme))
            binding.editTextValue.setTextColor(ContextCompat.getColor(binding.root.context, R.color.green_theme))
        } else {
            binding.textViewValuePrefix.text = "- R$"
            binding.textViewValuePrefix.setTextColor(ContextCompat.getColor(binding.root.context, R.color.pink_theme))
            binding.editTextValue.setTextColor(ContextCompat.getColor(binding.root.context, R.color.pink_theme))
        }
    }

    fun onSave(){
        val entry: Entry = Entry(
            null,
            OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS).with(LocalDate.parse(binding.textViewDate.text, DateTimeFormatter.ofPattern("dd/MM/yyyy"))),
            if(binding.switchEntryType.isChecked) binding.editTextValue.text.toString().toFloat() else binding.editTextValue.text.toString().toFloat().times(-1),
            binding.textInputDescription.editText?.text.toString()
        )
        entryDetailViewModel.save(entry)
    }
}