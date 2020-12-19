package me.miguelos.sample.presentation.ui.characters.adapter

import android.annotation.SuppressLint
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.recyclerview.widget.RecyclerView
import me.miguelos.sample.databinding.ItemCharacterBinding
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.util.imageloader.ImageLoader

class MarvelCharacterViewHolder(
    private val itemBinding: ItemCharacterBinding,
    private val listener: MarvelCharactersAdapter.CharacterItemListener,
    private val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(itemBinding.root) {

    private lateinit var character: MarvelCharacter

    @SuppressLint("SetTextI18n")
    fun bind(
        item: MarvelCharacter,
        selectionEnabled: Boolean,
        isChecked: Boolean,
        canSelect: Boolean
    ) {
        this.character = item
        itemBinding.listItemNameTv.text = item.name
        imageLoader.loadCircleImage(itemBinding.listItemImageIv, item.thumbnail)
        itemBinding.listItemCb.isChecked = isChecked
        if (selectionEnabled) {
            itemBinding.listItemCb.apply {
                visibility = VISIBLE
                isEnabled = canSelect || itemBinding.listItemCb.isChecked
                setOnClickListener {
                    listener.onCheckedCharacter(item, itemBinding.listItemCb.isChecked)
                }
            }
        } else {
            itemBinding.listItemCb.visibility = GONE
        }
        itemBinding.root.setOnClickListener {
            listener.onClickedCharacter(character)
        }
    }
}
