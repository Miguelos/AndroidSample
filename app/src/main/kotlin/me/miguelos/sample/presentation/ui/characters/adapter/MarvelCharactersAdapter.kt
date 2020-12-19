package me.miguelos.sample.presentation.ui.characters.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.miguelos.sample.databinding.ItemCharacterBinding
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.util.imageloader.ImageLoader


class MarvelCharactersAdapter(
    private val listener: CharacterItemListener,
    private val imageLoader: ImageLoader,
    private val selectionEnabled: Boolean
) : RecyclerView.Adapter<MarvelCharacterViewHolder>() {

    interface CharacterItemListener {
        fun onClickedCharacter(character: MarvelCharacter)
        fun onCheckedCharacter(character: MarvelCharacter, checked: Boolean)
    }

    private val items = ArrayList<MarvelCharacter>(20)
    private val checkedItems = ArrayList<MarvelCharacter>(2)


    init {
        setHasStableIds(true)
    }

    fun addItems(items: ArrayList<MarvelCharacter>) {
        val prevSize = this.items.size
        this.items.addAll(
            items.filter { it !in this.checkedItems }
        )
        notifyItemRangeChanged(prevSize, items.size)
    }

    fun checkItem(character: MarvelCharacter, checked: Boolean) {
        if (checked) {
            checkedItems.add(character)
        } else {
            checkedItems.remove(character)
        }
    }

    fun getCheckedItemCount() = checkedItems.size

    fun getCheckedItems(i: Int) =
        if (i in this.checkedItems.indices) {
            checkedItems[i]
        } else {
            null
        }

    fun clear() {
        this.items.clear()
        this.items.addAll(this.checkedItems)
    }

    override fun getItemId(position: Int) =
        if (position in this.items.indices) {
            this.items[position].id
        } else {
            -1
        }

    fun getItem(position: Int): MarvelCharacter? = if (position in this.items.indices) {
        this.items[position]
    } else {
        null
    }

    override fun getItemViewType(position: Int) = position

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarvelCharacterViewHolder {
        val binding: ItemCharacterBinding =
            ItemCharacterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return MarvelCharacterViewHolder(binding, listener, imageLoader)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holderMarvel: MarvelCharacterViewHolder, position: Int): Unit =
        holderMarvel.bind(
            items[position],
            selectionEnabled,
            items[position] in checkedItems,
            checkedItems.size < 2
        )
}
