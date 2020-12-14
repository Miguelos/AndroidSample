package me.miguelos.sample.presentation.ui.characterdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.databinding.CharacterDetailFragmentBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.util.autoCleared
import me.miguelos.sample.util.imageloader.ImageLoader
import javax.inject.Inject


@AndroidEntryPoint
class CharacterDetailFragment : BaseFragment() {

    private var binding by autoCleared<CharacterDetailFragmentBinding>()
    private val viewModel: CharacterDetailViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharacterDetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        observeViewState()
        arguments?.getLong(ARG_ID)?.let { viewModel.saveId(it) }
    }

    private fun observeViewState() {
        viewModel.characterState.observe(viewLifecycleOwner) { handleCharacterState(it) }
        viewModel.viewState.observe(viewLifecycleOwner) { handleViewState(it) }
        viewModel.errorState.observe(viewLifecycleOwner) { handleFeedbackState(it) }
    }

    private fun handleCharacterState(character: MarvelCharacter) {
        binding.detailNameTv.text = character.name
        binding.detailDescriptionTv.text = character.description
        imageLoader.loadImage(binding.detailImageIv, character.thumbnail)
    }

    private fun handleFeedbackState(error: Throwable) {
        Snackbar.make(
            binding.characterCl,
            "Error: ${error.localizedMessage}",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleViewState(viewState: CharacterViewState) {
        binding.detailPb.visibility = if (viewState.isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    companion object {
        const val ARG_ID = "character_id"
    }
}
