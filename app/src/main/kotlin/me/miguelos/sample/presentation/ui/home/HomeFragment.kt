package me.miguelos.sample.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.FragmentHomeBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.util.autoCleared
import me.miguelos.sample.util.showSnackbar


@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding by autoCleared()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initButtons()
    }

    private fun initButtons() {
        binding.searchButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_charactersFragment
            )
        }
        listOf(binding.arenaButton, binding.rankingButton).forEach {
            it.setOnClickListener {
                binding.homeCl.showSnackbar("Under development")
            }
        }
    }
}
