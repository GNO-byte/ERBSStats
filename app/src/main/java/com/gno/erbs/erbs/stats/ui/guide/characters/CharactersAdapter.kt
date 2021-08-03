package com.gno.erbs.erbs.stats.ui.guide.characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.characters.Character


class CharactersAdapter(
    private val cellClickListener: (Int) -> Unit
) : ListAdapter<Character, RecyclerView.ViewHolder>(CharactersDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_CHARACTER = 2
    }

//    private val shimmerDrawable: ShimmerDrawable =
//        ShimmerDrawable().apply {
//            val shimmer =
//                Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
//                    .setDuration(1800) // how long the shimmering animation takes to do one full sweep
//                    .setBaseAlpha(0.7f) //the alpha of the underlying children
//                    .setHighlightAlpha(0.6f) // the shimmer alpha amount
//                    .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
//                    .setAutoStart(true)
//                    .build()
//
//            setShimmer(shimmer)
//        }

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

//                var shimmerDrawable = ShimmerDrawable().apply {
//                    val shimmer =
//                        Shimmer.ColorHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
//                            .setDuration(1800) // how long the shimmering animation takes to do one full sweep
//                            .setBaseAlpha(0.7f) //the alpha of the underlying children
//                            .setHighlightAlpha(0.6f) // the shimmer alpha amount
//                            .setBaseColor(
//                                ContextCompat.getColor(
//                                    characterHolder.image.context,
//                                    R.color.goldenrod
//                                )
//                            )
//                            .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
//                            .setAutoStart(true)
//                            .build()
//
//                    setShimmer(shimmer)
//                }
//
//
//                val requestOptions = RequestOptions()
//                requestOptions.placeholder(shimmerDrawable)


                Glide.with(characterHolder.image.context)
//                    .setDefaultRequestOptions(requestOptions)
                    .load(item.iconWebLink)
                    .placeholder(R.drawable.loading_image)
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