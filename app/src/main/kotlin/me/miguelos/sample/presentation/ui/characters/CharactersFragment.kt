package me.miguelos.sample.presentation.ui.characters

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.FragmentCharactersBinding
import me.miguelos.sample.presentation.core.BaseFragment
import me.miguelos.sample.presentation.core.EndlessRecyclerViewScrollListener
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.presentation.ui.MainActivity
import me.miguelos.sample.presentation.ui.MainActivity.Companion.ARG_ID
import me.miguelos.sample.presentation.ui.arena.ArenaFragment
import me.miguelos.sample.presentation.ui.characters.adapter.MarvelCharactersAdapter
import me.miguelos.sample.util.ErrorMessageFactory
import me.miguelos.sample.util.autoCleared
import me.miguelos.sample.util.imageloader.ImageLoader
import me.miguelos.sample.util.showSnackbar
import java.net.UnknownHostException
import javax.inject.Inject


@AndroidEntryPoint
class CharactersFragment : BaseFragment(), MarvelCharactersAdapter.CharacterItemListener {

    private var binding: FragmentCharactersBinding by autoCleared()
    private val viewModel: CharactersViewModel by viewModels()

    private var characterAdapterMarvel: MarvelCharactersAdapter? = null

    @Inject
    lateinit var imageLoader: ImageLoader


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharactersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {
        (requireActivity() as? MainActivity)?.updateTitle(getString(R.string.title_fragment_list))

        arguments?.getBoolean(ARG_SELECTION_ENABLED)?.let {
            if (it) {
                (requireActivity() as? MainActivity)?.updateTitle(getString(R.string.select_characters_title))
                viewModel.enableSelection()
            }
        }

        arguments?.getBoolean(ARG_RANKING_ENABLED)?.let {
            if (it) {
                (requireActivity() as? MainActivity)?.updateTitle(getString(R.string.arena_ranking_title))

                viewModel.enableRanking()
                characterAdapterMarvel?.clear()
                loadMarvelCharacters()
            }
        }

        initAdapter()
        initSearch()
        initSelectUi()
        observeViewState()
    }

    private fun initSelectUi() {
        if (viewModel.isSelectionEnabled()) {
            binding.selectListItemsB.visibility = VISIBLE
            binding.selectListItemsB.setOnClickListener {
                characterAdapterMarvel?.let { adapter ->
                    setFragmentResult(
                        ArenaFragment.MARVEL_CHARACTER_REQUEST_KEY,
                        bundleOf(
                            ArenaFragment.MARVEL_CHARACTER_1_KEY to adapter.getCheckedItems(0),
                            ArenaFragment.MARVEL_CHARACTER_2_KEY to adapter.getCheckedItems(1)
                        )
                    )
                    findNavController().popBackStack()
                }
            }
            updateGoToArenaButton()
        } else {
            binding.selectListItemsB.visibility = GONE
        }
    }

    private fun initAdapter() {
        if (characterAdapterMarvel == null) {
            characterAdapterMarvel =
                MarvelCharactersAdapter(this, imageLoader, viewModel.isSelectionEnabled())
        }

        characterAdapterMarvel?.let {
            binding.charactersRv.apply {
                val linearLayoutManager = LinearLayoutManager(requireContext())
                layoutManager = linearLayoutManager
                adapter = it
                addOnScrollListener(object :
                    EndlessRecyclerViewScrollListener(linearLayoutManager) {
                    override fun onLoadMore(
                        page: Int,
                        totalItemsCount: Int,
                        view: RecyclerView?
                    ) {
                        viewModel.loadCharacters(totalItemsCount = totalItemsCount)
                    }
                })
            }
        }
    }

    private fun initSearch() {
        binding.searchEt.apply {
            setOnEditorActionListener { _, actionId, _ ->
                if (
                    actionId == EditorInfo.IME_ACTION_GO
                    || actionId == EditorInfo.IME_ACTION_SEARCH
                ) {
                    loadMarvelCharacters()
                    true
                } else {
                    false
                }
            }
            setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    loadMarvelCharacters()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun loadMarvelCharacters() {
        binding.charactersRv.scrollToPosition(0)
        viewModel.loadCharacters(getSearchQuery().toString())
        characterAdapterMarvel?.apply {
            clear()
            notifyDataSetChanged()
        }
        binding.charactersRv.recycledViewPool.clear()
    }

    private fun getSearchQuery() =
        binding.searchEt.text.trim()

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
        characterAdapterMarvel?.addItems(ArrayList(characters))
        binding.emptyListTv.visibility =
            if (characterAdapterMarvel != null && characterAdapterMarvel!!.itemCount > 0) {
                GONE
            } else {
                VISIBLE
            }
    }

    private fun handleFeedbackState(error: Throwable) {
        if (error is UnknownHostException) {
            (requireActivity() as? MainActivity)?.showDialog(
                getString(R.string.dialog_no_internet_connection)
            )
        } else {
            binding.charactersCl.showSnackbar(ErrorMessageFactory.create(requireContext(), error))
        }
    }

    private fun handleViewState(viewState: CharactersViewState) {
        binding.listPb.visibility = if (viewState.isLoading) {
            VISIBLE
        } else {
            GONE
        }
    }

    override fun onCheckedCharacter(character: MarvelCharacter, checked: Boolean) {
        characterAdapterMarvel?.let { adapter ->
            adapter.checkItem(character, checked)
            adapter.notifyDataSetChanged()
            updateGoToArenaButton()
        }
    }

    private fun updateGoToArenaButton() {
        if (characterAdapterMarvel?.getCheckedItemCount() == 2) {
            binding.selectListItemsB.isEnabled = true
            binding.selectListItemsB.text = getString(R.string.go_to_arena_button)
        } else {
            binding.selectListItemsB.isEnabled = false
            binding.selectListItemsB.text = getString(R.string.select_two_characters_button)
        }
    }

    override fun onClickedCharacter(character: MarvelCharacter) {
        findNavController().navigate(
            R.id.action_charactersFragment_to_characterDetailFragment,
            bundleOf(ARG_ID to character.id)
        )
    }

    companion object {
        const val ARG_SELECTION_ENABLED = "enable_selection"
        const val ARG_RANKING_ENABLED = "enable_ranking"
    }
}
