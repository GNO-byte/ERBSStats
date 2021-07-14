package com.gno.erbs.erbs.stats.ui.guide.characters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.gno.erbs.erbs.stats.R
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.model.erbs.characters.Character

import com.squareup.picasso.Picasso

class CharactersAdapter(
    private val cellClickListener: (Int) -> Unit
    )
    : ListAdapter<Character, CharactersAdapter.ViewHolder>(CharactersDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_character, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        Glide.with(holder.image.context).load(item.iconWebLink).circleCrop().into(holder.image)

        holder.image.setOnClickListener {
            cellClickListener.invoke(item.code)
        }
    }

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.character_guide_image)
    }
}