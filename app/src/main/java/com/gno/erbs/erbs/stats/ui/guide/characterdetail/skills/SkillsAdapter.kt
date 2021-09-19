package com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.facebook.shimmer.ShimmerFrameLayout
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.drive.corecharacter.CoreSkill
import com.gno.erbs.erbs.stats.ui.LoadingImageHelper
import com.gno.erbs.erbs.stats.ui.base.BaseListAdapter
import javax.inject.Inject

class SkillsAdapter @Inject constructor(loadingImageHelper: LoadingImageHelper) :
    BaseListAdapter<CoreSkill, RecyclerView.ViewHolder>(
        SkillsDiffUtilCallback(),
        loadingImageHelper
    ) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_SKILL = 2
    }

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item == null) {
            true -> TYPE_LOADING
            false -> TYPE_SKILL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_skill, parent, false)
            )
            TYPE_SKILL -> SkillHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_skill, parent, false)
            )
            else -> throw RuntimeException("Type does not fit")
        }
    }

    fun addLoading() {
        val currentList = currentList.toMutableList()
        currentList.add(null)
        currentList.add(null)
        currentList.add(null)
        submitList(currentList)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val item = getItem(position)


        when (holder.itemViewType) {

            TYPE_SKILL -> {

                val rankHolder = holder as SkillHolder

                rankHolder.skillName.text = item.name
                rankHolder.skillType.text = item.type
                rankHolder.skillDescription.text = item.description

                rankHolder.loading.visibility = View.VISIBLE
                rankHolder.skillImage.visibility = View.GONE

                loadImage(rankHolder.skillImage, item.image, rankHolder.loading)

            }

        }
    }

    class SkillHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val skillName: TextView = itemView.findViewById(R.id.name)
        var skillType: TextView = itemView.findViewById(R.id.type)
        var skillDescription: TextView = itemView.findViewById(R.id.description)
        val skillImage: ImageView = itemView.findViewById(R.id.image)
        val loading: ShimmerFrameLayout = itemView.findViewById(R.id.loading)

    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}