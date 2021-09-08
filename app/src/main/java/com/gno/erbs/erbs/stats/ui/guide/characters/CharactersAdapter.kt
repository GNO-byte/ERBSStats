package com.gno.erbs.erbs.stats.ui.guide.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.characters.Character
import com.gno.erbs.erbs.stats.ui.base.BaseListAdapter
import com.google.android.material.imageview.ShapeableImageView


class CharactersAdapter(
    private val cellClickListener: (Int, String) -> Unit
) : BaseListAdapter<Character, RecyclerView.ViewHolder>(CharactersDiffUtilCallback()) {

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

                loadImage(characterHolder.image,item.iconWebLink,characterHolder.loading)

                characterHolder.image.setOnClickListener {
                    cellClickListener.invoke(item.code,item.name)
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
        val loading: ShimmerFrameLayout = itemView.findViewById(R.id.loading)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


}