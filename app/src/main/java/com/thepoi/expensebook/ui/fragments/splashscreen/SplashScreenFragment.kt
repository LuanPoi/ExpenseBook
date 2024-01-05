package com.thepoi.expensebook.ui.fragments.splashscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.thepoi.expensebook.R
import com.thepoi.expensebook.databinding.FragmentSplashScreenBinding
import dagger.hilt.android.AndroidEntryPoint
import java.time.YearMonth
import java.time.ZoneId

@AndroidEntryPoint
class SplashScreenFragment : Fragment() {

    private lateinit var binding: FragmentSplashScreenBinding
    private lateinit var viewModel: SplashScreenViewModel

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[SplashScreenViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_splash_screen,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.viewModel.checkMonthlyExpenseExistence().observe(viewLifecycleOwner){
            if (it?.yearMonth == YearMonth.now(ZoneId.systemDefault())) findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
            else findNavController().navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToInitializeMonthFragment(it?.yearMonth))
        }
    }
}