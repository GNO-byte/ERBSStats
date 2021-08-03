package com.gno.erbs.erbs.stats.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.FoundObject

class SearchAdapter(
    private val cellClickListener: (FoundObject) -> Unit
) : ListAdapter<FoundObject, RecyclerView.ViewHolder>(SearchDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_FOUND_OBJECT = 2
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item == null) {
            true -> TYPE_LOADING
            false -> TYPE_FOUND_OBJECT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_found_object, parent, false)
            )
            TYPE_FOUND_OBJECT -> FoundObjectHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_found_object, parent, false)
            )
            else -> throw RuntimeException("Type does not fit")
        }
    }

    fun addLoading(){
        val currentList = currentList.toMutableList()
        currentList.add(null)
        currentList.add(null)
        currentList.add(null)
        submitList(currentList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (holder.itemViewType) {
            TYPE_FOUND_OBJECT -> {
                val foundObjectHolder = holder as FoundObjectHolder
                foundObjectHolder.name.text = item.name
                foundObjectHolder.type.text = item.type.title
                foundObjectHolder.cardView.setOnClickListener {
                    cellClickListener.invoke(item)
                }
            }
        }
    }

    class FoundObjectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView: CardView = itemView.findViewById(R.id.cardview)
        val name: TextView = itemView.findViewById(R.id.name)
        val type: TextView = itemView.findViewById(R.id.type)
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}
