package me.miguelos.sample.presentation.ui.characters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import me.miguelos.sample.databinding.ItemCharacterBinding
import me.miguelos.sample.presentation.model.MarvelCharacter
import me.miguelos.sample.util.imageloader.ImageLoader


class CharactersAdapter(
    private val listener: CharacterItemListener,
    private val imageLoader: ImageLoader
) : RecyclerView.Adapter<CharactersAdapter.CharacterViewHolder>() {

    interface CharacterItemListener {
        fun onClickedCharacter(characterId: Long)
    }

    private val items = ArrayList<MarvelCharacter>()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding: ItemCharacterBinding =
            ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding, listener, imageLoader)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) =
        holder.bind(items[position])

    class CharacterViewHolder(
        private val itemBinding: ItemCharacterBinding,
        private val listener: CharacterItemListener,
        private val imageLoader: ImageLoader
    ) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

        private lateinit var character: MarvelCharacter

        init {
            itemBinding.root.setOnClickListener(this)
        }

        @SuppressLint("SetTextI18n")
        fun bind(item: MarvelCharacter) {
            this.character = item
            itemBinding.listItemNameTv.text = item.name
            imageLoader.loadCircleImage(itemBinding.listItemImageIv, item.thumbnail)
        }

        override fun onClick(v: View?) {
            listener.onClickedCharacter(character.id)
        }
    }
}
