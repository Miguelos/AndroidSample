package me.miguelos.sample.presentation.ui.characters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.CharactersFragmentBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.core.EndlessRecyclerViewScrollListener
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.presentation.ui.MainActivity.Companion.ARG_ID
import me.miguelos.sample.util.ErrorMessageFactory
import me.miguelos.sample.util.autoCleared
import me.miguelos.sample.util.imageloader.ImageLoader
import javax.inject.Inject


@AndroidEntryPoint
class CharactersFragment : BaseFragment(), CharactersAdapter.CharacterItemListener {

    private var binding: CharactersFragmentBinding by autoCleared()
    private val viewModel: CharactersViewModel by viewModels()

    private var characterAdapter: CharactersAdapter? = null

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
        viewModel.loadMoreCharacters(0)
    }

    private fun initAdapter() {
        if (characterAdapter == null) {
            characterAdapter = CharactersAdapter(this, imageLoader)
        }
        binding.charactersRv.apply {
            val linearLayoutManager = LinearLayoutManager(requireContext())
            layoutManager = linearLayoutManager
            adapter = characterAdapter
            addOnScrollListener(object :
                EndlessRecyclerViewScrollListener(linearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    viewModel.loadMoreCharacters(totalItemsCount)
                }
            })
        }
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
        binding.emptyListTv.visibility = if (characters.isEmpty()) {
            View.VISIBLE
        } else {
            characterAdapter?.addItems(ArrayList(characters))
            View.GONE
        }
    }

    private fun handleFeedbackState(error: Throwable) {
        Snackbar.make(
            binding.charactersCl,
            ErrorMessageFactory.create(requireContext(), error),
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
