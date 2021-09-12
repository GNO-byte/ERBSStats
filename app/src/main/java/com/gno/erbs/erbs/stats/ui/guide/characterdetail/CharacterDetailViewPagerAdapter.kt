package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.background.BackgroundFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.characterstats.CharacterStatsFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills.SkillsFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes.WeaponTypesFragment

class CharacterDetailViewPagerAdapter(
    val supportFragmentManager: FragmentManager,
    val viewLifecycleOwner: Lifecycle,
) : FragmentStateAdapter(supportFragmentManager, viewLifecycleOwner) {

    data class Page(val title: String, val ctor: () -> Fragment)

    @Suppress("MoveLambdaOutsideParentheses")
    private val pages = listOf(
        Page(
            "Background",
            { BackgroundFragment.newInstance() }
        ),
        Page(
            "Stats",
            { CharacterStatsFragment.newInstance() }
        ),
        Page(
            "Weapons",
            { WeaponTypesFragment.newInstance() }
        ),
        Page(
            "Skills",
            { SkillsFragment.newInstance() }
        ),

        )


    override fun createFragment(position: Int): Fragment {
        return pages[position].ctor()
    }

    override fun getItemCount(): Int {
        return pages.size
    }


    fun getPageTitle(position: Int): String {
        return pages[position].title
    }
}
