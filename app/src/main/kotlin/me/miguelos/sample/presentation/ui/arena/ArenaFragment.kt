package me.miguelos.sample.presentation.ui.arena

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.FragmentArenaBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.presentation.ui.MainActivity
import me.miguelos.sample.presentation.ui.characters.CharactersFragment
import me.miguelos.sample.util.ErrorMessageFactory
import me.miguelos.sample.util.autoCleared
import me.miguelos.sample.util.imageloader.ImageLoader
import me.miguelos.sample.util.showSnackbar
import javax.inject.Inject


@AndroidEntryPoint
class ArenaFragment : BaseFragment() {

    private var binding by autoCleared<FragmentArenaBinding>()
    private val viewModel: ArenaViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArenaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        (requireActivity() as? MainActivity)?.updateTitle(getString(R.string.arena_title))

        initButtons()
        observeViewState()
    }

    private fun initButtons() {
        setFragmentResultListener(MARVEL_CHARACTER_REQUEST_KEY) { key, bundle ->
            if (key == MARVEL_CHARACTER_REQUEST_KEY) {
                (bundle[MARVEL_CHARACTER_1_KEY] as? MarvelCharacter)?.let { first ->
                    (bundle[MARVEL_CHARACTER_2_KEY] as? MarvelCharacter)?.let { second ->
                        viewModel.saveFighters(first, second)
                    }
                }
            }
        }

        binding.selectFightersB.setOnClickListener {
            findNavController().navigate(
                R.id.action_arenaFragment_to_charactersFragment,
                bundleOf(CharactersFragment.ARG_SELECTION_ENABLED to true)
            )
        }

        binding.fightB.setOnClickListener {
            binding.selectFightersB.isEnabled = false
            showResult()
        }

        binding.goToRankingB.setOnClickListener {
            findNavController().navigate(
                R.id.action_arenaFragment_to_charactersRankingFragment,
                bundleOf(CharactersFragment.ARG_RANKING_ENABLED to true)
            )
        }
    }

    private fun showResult() {
        val winner = viewModel.getWinner()
        listOf(binding.goToRankingB, binding.winnerLabelTv, binding.winnerTv).forEach {
            it.visibility = VISIBLE
        }
        if (winner == null) {
            binding.winnerLabelTv.text = getString(R.string.tie_message)
            binding.winnerTv.text = ""
        } else {
            binding.winnerLabelTv.text = getString(R.string.winner_label)
            binding.winnerTv.text = winner.name
        }
        binding.selectFightersB.isEnabled = false
        binding.fightB.visibility = GONE
    }

    private fun observeViewState() {
        viewModel.viewState.observe(
            viewLifecycleOwner,
            { handleViewState(it) }
        )

        viewModel.errorState.observe(
            viewLifecycleOwner,
            { handleFeedbackState(it) }
        )

        viewModel.charactersState.observe(
            viewLifecycleOwner,
            { handleCharactersState(it) }
        )
    }

    private fun handleCharactersState(characters: Pair<MarvelCharacter, MarvelCharacter>) {
        binding.fighter1Tv.text = characters.first.name
        imageLoader.loadCircleImage(binding.fighter1Iv, characters.first.thumbnail)
        binding.fighter2Tv.text = characters.second.name
        imageLoader.loadCircleImage(binding.fighter2Iv, characters.second.thumbnail)
        binding.fightB.isEnabled = true
    }

    private fun handleFeedbackState(error: Throwable) {
        binding.arenaCl.showSnackbar(ErrorMessageFactory.create(requireContext(), error))
    }

    private fun handleViewState(viewState: ArenaViewState) {
        binding.arenaPb.visibility = if (viewState.isLoading) {
            VISIBLE
        } else {
            GONE
        }
    }

    companion object {
        const val MARVEL_CHARACTER_REQUEST_KEY = "marvel_character_request_key"
        const val MARVEL_CHARACTER_1_KEY = "marvel_character_1_key"
        const val MARVEL_CHARACTER_2_KEY = "marvel_character_2_key"
    }
}
