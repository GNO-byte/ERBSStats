package com.gno.erbs.erbs.stats.ui.guide.characters

import android.content.Context
import android.graphics.drawable.InsetDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.ui.base.BaseAdapter
import com.google.android.material.color.MaterialColors


class CharactersAdapter(
    private val cellClickListener: (Int) -> Unit
) : BaseAdapter<Character, RecyclerView.ViewHolder>(CharactersDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_CHARACTER = 2
    }

    fun addLoading() {
        val currentList = currentList.toMutableList()

        repeat(10) {
            currentList.add(null)
        }

        submitList(currentList)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item == null) {
            true -> TYPE_LOADING
            false -> TYPE_CHARACTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_character, parent, false)
            )
            TYPE_CHARACTER -> CharacterHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_character, parent, false)
            )
            else -> throw RuntimeException("Type does not fit")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder.itemViewType) {

            TYPE_CHARACTER -> {

                val characterHolder = holder as CharacterHolder


                Glide.with(characterHolder.image.context)
                    .load(item.iconWebLink)
                    .placeholder(createShimmer(characterHolder.image.context))
                    .error(R.drawable.loading_image)
                    .circleCrop()
                    .into(characterHolder.image)

                characterHolder.image.setOnClickListener {
                    cellClickListener.invoke(item.code)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    class CharacterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.character_guide_image)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}