package me.miguelos.sample.presentation.ui.arena

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.FragmentArenaBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.presentation.ui.characters.CharactersFragment
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
        initButtons()
        observeViewState()
    }

    private fun initButtons() {
        setFragmentResultListener(MARVEL_CHARACTER_REQUEST_KEY) { key, bundle ->
            if (key == MARVEL_CHARACTER_REQUEST_KEY) {
                (bundle[MARVEL_CHARACTER_1_KEY] as? MarvelCharacter)?.let {
                    handleFighterState(true, it)
                }
                (bundle[MARVEL_CHARACTER_2_KEY] as? MarvelCharacter)?.let {
                    handleFighterState(false, it)
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
            binding.arenaCl.showSnackbar("Feature under development")
        }
    }

    private fun observeViewState() {
        // no-op
    }

    private fun handleFighterState(first: Boolean, character: MarvelCharacter) {
        val iv: ImageView
        val tv: TextView
        if (first) {
            tv = binding.fighter1Tv
            iv = binding.fighter1Iv
        } else {
            tv = binding.fighter2Tv
            iv = binding.fighter2Iv
        }
        tv.text = character.name
        imageLoader.loadCircleImage(iv, character.thumbnail)
    }

    companion object {
        const val MARVEL_CHARACTER_REQUEST_KEY = "marvel_character_request_key"
        const val MARVEL_CHARACTER_1_KEY = "marvel_character_1_key"
        const val MARVEL_CHARACTER_2_KEY = "marvel_character_2_key"
    }
}
