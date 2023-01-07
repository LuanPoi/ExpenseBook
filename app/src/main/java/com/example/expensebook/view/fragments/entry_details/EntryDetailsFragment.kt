package com.example.expensebook.view.fragments.entry_details

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
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.repository.EntryRepository
import com.example.expensebook.view.fragments.home.HomeViewModel
import java.lang.Float
import java.time.OffsetDateTime
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
        }

        binding.buttonConfirm.setOnClickListener { onSave().also { findNavController().navigateUp() } }
        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun bindUiState(uiState: EntryDetailsUiState){
        binding.textViewDate.setText(uiState.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
        binding.switchEntryType.isChecked = (uiState.value >= 0f) //true=receipt | false=expense
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