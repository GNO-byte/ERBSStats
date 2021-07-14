package com.gno.erbs.erbs.stats.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.MenuObject

class HomeAdapter(
    private val cellClickListener: (Int) -> Unit
) : ListAdapter<MenuObject, HomeAdapter.ViewHolder>(HomeDiffUtilCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_menu_object, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        holder.name.text = item.name

        holder.constraintLayout.setOnClickListener {
            cellClickListener.invoke(item.navigationLink)
        }


    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val constraintLayout: ConstraintLayout = view.findViewById(R.id.constraintlayout)
        val name: TextView = view.findViewById(R.id.name)
    }


}
