package me.miguelos.sample.presentation.ui.arena

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import me.miguelos.sample.databinding.FragmentArenaBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.util.autoCleared
import me.miguelos.sample.util.imageloader.ImageLoader
import javax.inject.Inject


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
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.charactersState.observe(viewLifecycleOwner) { handleArenaState(it) }
    }

    private fun handleArenaState(characters: Pair<MarvelCharacter, MarvelCharacter>) {
        // no-op
    }

    private fun handleFeedbackState(error: Throwable) {
        // no-op
    }
}
