package me.miguelos.sample.presentation.ui.characters.adapter

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import me.miguelos.sample.databinding.ItemCharacterBinding
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.util.imageloader.ImageLoader

class CharacterViewHolder(
    private val itemBinding: ItemCharacterBinding,
    private val listener: CharactersAdapter.CharacterItemListener,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var character: MarvelCharacter

    init {
        itemBinding.root.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(item: MarvelCharacter, isSelected: Boolean? = false) {
        this.character = item
        itemBinding.listItemNameTv.text = item.name
        isSelected?.let {
            itemBinding.listItemCl.isActivated = it
        }
        imageLoader.loadCircleImage(itemBinding.listItemImageIv, item.thumbnail)
    }

    override fun onClick(v: View?) {
        listener.onClickedCharacter(character.id)
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> =
        object : ItemDetailsLookup.ItemDetails<Long>() {
            override fun getPosition(): Int = adapterPosition
            override fun getSelectionKey(): Long = itemId
        }
}
