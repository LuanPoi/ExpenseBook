package com.example.expensebook.view.fragments.entry_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import com.example.expensebook.databinding.FragmentEntryDetailsBinding
import com.example.expensebook.model.entity.Entry
import com.example.expensebook.view.fragments.home.HomeViewModel
import java.lang.Float
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class EntryDetailsFragment : Fragment() {

    private lateinit var binding: FragmentEntryDetailsBinding
    private lateinit var entryDetailViewModel: EntryDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        entryDetailViewModel = EntryDetailsViewModel(requireActivity().application)
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

        entryDetailViewModel.entry.value.let {
            if (it != null){
                binding.textViewDate.setText(it.date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                binding.switchEntryType.isChecked = (it.value >= 0f) //true=receipt | false=expense
                binding.editTextValue.setText(it.value.toString())
                binding.textInputDescription.editText?.setText(it.description)
            }
        }

        binding.buttonConfirm.setOnClickListener { save().also { findNavController().navigateUp() } }
        binding.buttonCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    fun save(){
        val entry = entryDetailViewModel.entry.value ?: return
//        TODO: add date edit possibility
        entry.value = if(binding.switchEntryType.isChecked) binding.editTextValue.text.toString().toFloat() else binding.editTextValue.text.toString().toFloat().times(-1)
        entry.description = binding.textInputDescription.editText?.text.toString()
        entryDetailViewModel.entry.postValue(entry)
        entryDetailViewModel.save(entry)
    }
}