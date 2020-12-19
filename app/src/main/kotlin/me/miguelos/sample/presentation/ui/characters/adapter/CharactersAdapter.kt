package me.miguelos.sample.presentation.ui.characters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import me.miguelos.sample.databinding.ItemCharacterBinding
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.util.imageloader.ImageLoader


class CharactersAdapter(
    private val listener: CharacterItemListener,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<CharacterViewHolder>() {

    interface CharacterItemListener {
        fun onClickedCharacter(characterId: Long)
    }

    private val items = ArrayList<MarvelCharacter>()
    var selectionTracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)
    }

    fun setItems(items: ArrayList<MarvelCharacter>) {
        this.items.clear()
        this.addItems(items)
    }

    fun addItems(items: ArrayList<MarvelCharacter>) {
        val prevSize = this.items.size
        this.items.addAll(items)
        notifyItemRangeChanged(prevSize, items.size)
    }

    /**
     * Clear list
     */
    fun clear() {
        this.items.clear()
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int) =
        if (position in this.items.indices) {
            this.items[position].id
        } else {
            -1
        }

    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding: ItemCharacterBinding =
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return CharacterViewHolder(binding, listener, imageLoader)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int): Unit =
        holder.bind(items[position], selectionTracker?.isSelected(position.toLong()))
}