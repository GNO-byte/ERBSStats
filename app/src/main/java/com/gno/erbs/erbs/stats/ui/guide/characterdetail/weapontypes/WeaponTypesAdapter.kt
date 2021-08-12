package com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.model.erbs.characters.WeaponType
import com.gno.erbs.erbs.stats.ui.base.BaseListAdapter
import com.google.android.material.color.MaterialColors


class WeaponTypesAdapter :
    BaseListAdapter<WeaponType, RecyclerView.ViewHolder>(WeaponTypesDiffUtilCallback()) {

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_WEAPON_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return when (item == null) {
            true -> TYPE_LOADING
            false -> TYPE_WEAPON_TYPE
        }
    }

    override fun getItemCount(): Int {
        return when (val count = super.getItemCount()) {
            0 -> 0
            else -> count
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_LOADING -> LoadingHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.loading_item_weapon_type, parent, false)
            )
            TYPE_WEAPON_TYPE -> WeaponTypeHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_weapon_type, parent, false)
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

            TYPE_WEAPON_TYPE -> {

                val weaponTypeHolder = holder as WeaponTypeHolder

                weaponTypeHolder.weaponName.text = item.name

                weaponTypeHolder.weaponImage.visibility = View.GONE
                weaponTypeHolder.loading.visibility = View.VISIBLE

                loadImage(weaponTypeHolder.weaponImage,item.weaponTypeImageWebLink,weaponTypeHolder.loading)
                weaponTypeHolder.chart.fill(item)

            }
        }
    }



    class WeaponTypeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val weaponImage: ImageView = itemView.findViewById(R.id.weapon_image)
        val weaponName: TextView = itemView.findViewById(R.id.weapon_name)
        var chart: RadarChart = itemView.findViewById(R.id.chart)
        val loading: ShimmerFrameLayout = itemView.findViewById(R.id.loading)

        init {
            chart.configure()
        }

        private fun RadarChart.configure() {

            //.setBackgroundColor(Color.rgb(60, 65, 82));

            this.description.isEnabled = false

            this.webLineWidth = 1f;
            this.webColor = Color.LTGRAY;
            this.webLineWidthInner = 1f;
            this.webColorInner = Color.LTGRAY;
            this.webAlpha = 100;

            this.invalidate()

            this.legend.isEnabled = false

            val xAxis = this.xAxis
            xAxis.textSize = 9f
            xAxis.yOffset = 0f
            xAxis.xOffset = 0f

            xAxis.textColor = MaterialColors.getColor(
                context,
                R.attr.colorOnPrimary,
                ContextCompat.getColor(this.context, R.color.white)
            )

            val yAxis = this.yAxis
            yAxis.setLabelCount(5, false)
            yAxis.textSize = 9f
            yAxis.axisMinimum = 0f
            yAxis.axisMaximum = 3f
            yAxis.setDrawLabels(false)
            this.notifyDataSetChanged()


        }

    }

    private fun RadarChart.fill(weaponType: WeaponType) {

        val entries1: ArrayList<RadarEntry> = ArrayList()

        val xAxiss = mutableListOf<String>()

        //xAxiss.add("Assistance")
        xAxiss.add("As")
        entries1.add(RadarEntry(weaponType.assistance?.toFloat() ?: 0f))
        //xAxiss.add("Attack")
        xAxiss.add("A")
        entries1.add(RadarEntry(weaponType.attack?.toFloat() ?: 0f))
        //xAxiss.add("Control difficulty")
        xAxiss.add("CD")
        entries1.add(RadarEntry(weaponType.controlDifficulty?.toFloat() ?: 0f))
        //xAxiss.add("Defense")
        xAxiss.add("D")
        entries1.add(RadarEntry(weaponType.defense?.toFloat() ?: 0f))
        //xAxiss.add("Disruptor")
        xAxiss.add("Dis")
        entries1.add(RadarEntry(weaponType.disruptor?.toFloat() ?: 0f))
        //xAxiss.add("Move")
        xAxiss.add("M")
        entries1.add(RadarEntry(weaponType.move?.toFloat() ?: 0f))
        xAxiss.add("???")

        val set1 = RadarDataSet(entries1, weaponType.mastery)

        set1.color = MaterialColors.getColor(
            context,
            R.attr.colorSecondary,
            ContextCompat.getColor(this.context, R.color.goldenrod)
        )

        set1.fillColor = MaterialColors.getColor(
            context,
            R.attr.colorSecondary,
            ContextCompat.getColor(this.context, R.color.goldenrod)
        )

        set1.setDrawFilled(true)
        set1.fillAlpha = 180
        set1.lineWidth = 2f
        set1.isDrawHighlightCircleEnabled = true
        set1.setDrawHighlightIndicators(false)

        val sets: ArrayList<IRadarDataSet> = ArrayList()
        sets.add(set1)

        val data = RadarData(sets)
        data.setValueTextSize(8f)
        data.setDrawValues(false)
        //data.setValueTextColor(ContextCompat.getColor(this.context, R.color.white))

        this.data = data

        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return xAxiss[value.toInt()]
            }
        }
    }

    class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}