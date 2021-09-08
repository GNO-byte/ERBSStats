package com.gno.erbs.erbs.stats.ui.home.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.ui.base.BaseListAdapter

class HomeSliderAdapter :
    BaseListAdapter<String, RecyclerView.ViewHolder>(HomeSlideAdapterDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_ILLUSTRATION = 2
    }

    init {
        val currentList = currentList.toMutableList()
        repeat(5) {
            currentList.add(null)
        }
        submitList(currentList)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item == null) {
            true -> TYPE_LOADING
            false -> TYPE_ILLUSTRATION
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_illustration, parent, false)
            )
            TYPE_ILLUSTRATION -> IllustrationHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_illustration, parent, false)
            )
            else -> throw RuntimeException("Type does not fit")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder.itemViewType) {

            TYPE_ILLUSTRATION -> {

                val characterHolder = holder as IllustrationHolder
                loadImage(characterHolder.image, item, characterHolder.loading)

            }
        }
    }

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    class IllustrationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.illustration)
        val loading: ShimmerFrameLayout =
            itemView.findViewById(R.id.loading)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
