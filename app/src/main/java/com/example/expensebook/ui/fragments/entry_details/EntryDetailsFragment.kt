package com.example.expensebook.ui.fragments.entry_details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.expensebook.databinding.FragmentEntryDetailsBinding
import com.example.expensebook.data.model.entity.Entry
import com.example.expensebook.data.repository.EntryRepository
import com.example.expensebook.ui.fragments.home.HomeViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.lang.Float
import java.time.*
import java.time.Instant.ofEpochSecond
import java.time.format.DateTimeFormatter

class EntryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEntryDetailsBinding

    private val entryDetailViewModel: EntryDetailsViewModel by activityViewModels {
        EntryDetailsViewModel.Factory(EntryRepository(requireActivity().application))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            binding.switchEntryType.setOnClickListener { entryDetailViewModel.toggleEntryType(binding.switchEntryType.isChecked)}
            binding.buttonCalendar.setOnClickListener {
                val datePicker =
                    MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .build()
                parentFragmentManager.let {
                    datePicker.show(it, "teste")
                    datePicker.addOnPositiveButtonClickListener {
                        Log.d("date", LocalDateTime.from(Instant.ofEpochMilli(it).atOffset(
                            ZoneOffset.UTC)).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
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
        binding.switchEntryType.isChecked = uiState.isReceipt //true=receipt | false=expense
        binding.editTextValue.setText(uiState.value?.toString())
        binding.textInputDescription.editText?.setText(uiState.description)
    }

    fun onSave(){
        val entry: Entry = Entry(
            null,
            OffsetDateTime.now(),
            if(binding.switchEntryType.isChecked) binding.editTextValue.text.toString().toFloat() else binding.editTextValue.text.toString().toFloat().times(-1),
            binding.textInputDescription.editText?.text.toString()
        )
        entryDetailViewModel.save(entry)
    }
}