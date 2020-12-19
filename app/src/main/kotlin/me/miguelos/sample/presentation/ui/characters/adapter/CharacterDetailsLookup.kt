package me.miguelos.sample.presentation.ui.characters.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView


class CharacterDetailsLookup(private val recyclerView: RecyclerView) :
    ItemDetailsLookup<Long>() {

    override fun getItemDetails(event: MotionEvent): ItemDetails<Long>? =
        recyclerView.findChildViewUnder(event.x, event.y)?.let {
            (recyclerView.getChildViewHolder(it) as CharacterViewHolder).getItemDetails()
        }
}
