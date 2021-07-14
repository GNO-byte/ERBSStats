package com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill

class SkillsAdapter : ListAdapter<CoreSkill, SkillsAdapter.DataHolder>(SkillsDiffUtilCallback()) {

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataHolder {
        return DataHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_skill, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DataHolder, position: Int) {

        val item = getItem(position)
        holder.skillName.text = item.name
        holder.skillType.text = item.type
        holder.skillDescription.text = item.description

        Glide.with(holder.skillImage.context).load(item.image).circleCrop()
            .into(holder.skillImage)

    }

    class DataHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val skillName: TextView = itemView.findViewById(R.id.name)
        var skillType: TextView = itemView.findViewById(R.id.type)
        var skillDescription: TextView = itemView.findViewById(R.id.description)
        val skillImage: ImageView = itemView.findViewById(R.id.image)
        //var skillVideo: YouTubePlayerView = itemView.findViewById(R.id.video)

    }

}