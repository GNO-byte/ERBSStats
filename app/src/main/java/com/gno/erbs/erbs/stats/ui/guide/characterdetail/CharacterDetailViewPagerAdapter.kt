package com.gno.erbs.erbs.stats.ui.guide.characterdetail

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.background.BackgroundFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.characterstats.CharacterStatsFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills.SkillsFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes.WeaponTypesFragment

class CharacterDetailViewPagerAdapter(
    supportFragmentManager: FragmentManager,
) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    data class Page(val title: String, val ctor: () -> Fragment)

    @Suppress("MoveLambdaOutsideParentheses")
    private val pages = listOf(
        Page(
            "Background",
            { BackgroundFragment.newInstance() }
        ),
        Page(
            "Character stats",
            { CharacterStatsFragment.newInstance() }
        ),
        Page(
            "Weapon types",
            { WeaponTypesFragment.newInstance() }
        ),
        Page(
            "Skills",
            { SkillsFragment.newInstance() }
        ),

        )

    override fun getItem(position: Int): Fragment {
        return pages[position].ctor()
    }

    override fun getCount(): Int {
        return pages.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pages[position].title
    }
}
