package com.gno.erbs.erbs.stats.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.FoundObject
import com.gno.erbs.erbs.stats.model.MenuObject

class SearchAdapter(
    private val cellClickListener: (FoundObject) -> Unit
) : ListAdapter<FoundObject, SearchAdapter.ViewHolder>(SearchDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_found_object, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.name
        holder.type.text = item.type.title

        holder.cardView.setOnClickListener {
            cellClickListener.invoke(item)
        }


    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardview)
        val name: TextView = view.findViewById(R.id.name)
        val type: TextView = view.findViewById(R.id.type)


    }


}
