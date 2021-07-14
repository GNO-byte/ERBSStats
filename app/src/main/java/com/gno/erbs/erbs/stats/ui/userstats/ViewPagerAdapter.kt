package com.gno.erbs.erbs.stats.ui.userstats

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.ui.userstats.usercharecters.UserCharactersFragment
import com.gno.erbs.erbs.stats.ui.userstats.matches.MatchesFragment
import com.gno.erbs.erbs.stats.ui.userstats.overview.OverviewFragment

class ViewPagerAdapter(
    supportFragmentManager: FragmentManager,
    context: Context
) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    data class Page(val title: String, val ctor: () -> Fragment)

    @Suppress("MoveLambdaOutsideParentheses")
    private val pages = listOf(
        Page(
            context.getString(R.string.stats_tab_1),
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
