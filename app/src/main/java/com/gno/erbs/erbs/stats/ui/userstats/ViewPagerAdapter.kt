package com.gno.erbs.erbs.stats.ui.userstats

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.ui.userstats.usercharecters.UserCharactersFragment
import com.gno.erbs.erbs.stats.ui.userstats.matches.MatchesFragment
import com.gno.erbs.erbs.stats.ui.userstats.overview.OverviewFragment

class ViewPagerAdapter(
    supportFragmentManager: FragmentManager,
    val viewLifecycleOwner: Lifecycle,
) : FragmentStateAdapter(supportFragmentManager, viewLifecycleOwner) {

    data class Page(val title: String, val ctor: () -> Fragment)

    @Suppress("MoveLambdaOutsideParentheses")
    private val pages = listOf(
        Page(
            "Overview",
            { OverviewFragment.newInstance() }
        ),
        Page(
            "Characters",
            { UserCharactersFragment.newInstance() }
        ),
        Page(
            "Matches",
            { MatchesFragment.newInstance() }
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
