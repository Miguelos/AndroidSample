package me.miguelos.sample.presentation.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.CharactersFragmentBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.presentation.ui.characterdetail.CharacterDetailFragment.Companion.ARG_ID
import me.miguelos.sample.util.autoCleared
import me.miguelos.sample.util.imageloader.ImageLoader
import javax.inject.Inject


@AndroidEntryPoint
class CharactersFragment : BaseFragment(), CharactersAdapter.CharacterItemListener {

    private var binding: CharactersFragmentBinding by autoCleared()
    private val viewModel: CharactersViewModel by viewModels()

    private lateinit var characterAdapter: CharactersAdapter

    @Inject
    lateinit var imageLoader: ImageLoader


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CharactersFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        initAdapter()
        observeViewState()
        viewModel.getCharacterList()
    }

    private fun initAdapter() {
        characterAdapter = CharactersAdapter(this, imageLoader)
        binding.charactersRv.layoutManager = LinearLayoutManager(requireContext())
        binding.charactersRv.adapter = characterAdapter
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

    private fun handleCharactersState(characters: List<MarvelCharacter>) {
        characterAdapter.clear()
        characterAdapter.setItems(ArrayList(characters))
    }

    private fun handleFeedbackState(error: Throwable) {
        Snackbar.make(
            binding.charactersRv,
            "Error: ${error.localizedMessage}",
            Snackbar.LENGTH_LONG
        ).show()
    }

    private fun handleViewState(viewState: CharactersViewState) {
        binding.listPb.visibility = if (viewState.isLoading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    override fun onClickedCharacter(characterId: Long) {
        findNavController().navigate(
            R.id.action_charactersFragment_to_characterDetailFragment,
            bundleOf(ARG_ID to characterId)
        )
    }
}
