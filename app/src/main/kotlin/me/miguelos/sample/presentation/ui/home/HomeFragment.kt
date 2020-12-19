package me.miguelos.sample.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.FragmentHomeBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.ui.MainActivity
import me.miguelos.sample.presentation.ui.characters.CharactersFragment
import me.miguelos.sample.util.autoCleared


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
        (requireActivity() as? MainActivity)?.updateTitle(getString(R.string.title_fragment_home))

        initButtons()
    }

    private fun initButtons() {
        binding.listButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_charactersFragment
            )
        }
        binding.arenaButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_arenaFragment
            )
        }
        binding.rankingButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_homeFragment_to_charactersFragment,
                bundleOf(CharactersFragment.ARG_RANKING_ENABLED to true)
            )
        }
    }
}
