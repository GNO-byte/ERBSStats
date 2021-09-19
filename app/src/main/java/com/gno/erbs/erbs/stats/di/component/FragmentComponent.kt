package com.gno.erbs.erbs.stats.di.component

import androidx.fragment.app.Fragment
import com.gno.erbs.erbs.stats.di.module.FragmentModule
import com.gno.erbs.erbs.stats.ui.base.BaseFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.CharacterDetailFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.background.BackgroundFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.skills.SkillsFragment
import com.gno.erbs.erbs.stats.ui.guide.characterdetail.weapontypes.WeaponTypesFragment
import com.gno.erbs.erbs.stats.ui.guide.characters.CharactersFragment
import com.gno.erbs.erbs.stats.ui.home.HomeFragment
import com.gno.erbs.erbs.stats.ui.search.SearchDialogFragment
import com.gno.erbs.erbs.stats.ui.search.SearchFragment
import com.gno.erbs.erbs.stats.ui.top.TopFragment
import com.gno.erbs.erbs.stats.ui.userstats.UserStatsFragment
import com.gno.erbs.erbs.stats.ui.userstats.matches.MatchesFragment
import com.gno.erbs.erbs.stats.ui.userstats.usercharecters.UserCharactersFragment
import dagger.BindsInstance
import dagger.Subcomponent

@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(baseFragment: BaseFragment)

    fun inject(homeFragment: HomeFragment)

    fun inject(topFragment: TopFragment)

    fun inject(searchFragment: SearchFragment)
    fun inject(searchDialogFragment: SearchDialogFragment)

    fun inject(charactersFragment: CharactersFragment)

    fun inject(characterDetailFragment: CharacterDetailFragment)
    fun inject(backgroundFragment: BackgroundFragment)
    fun inject(skillsFragment: SkillsFragment)
    fun inject(weaponTypesFragment: WeaponTypesFragment)

    fun inject(userStatsFragment: UserStatsFragment)
    fun inject(userCharactersFragment: UserCharactersFragment)
    fun inject(matchesFragment: MatchesFragment)


    @Subcomponent.Builder
    interface Builder {

        @BindsInstance
        fun fragment(fragment: Fragment): Builder

        fun build(): FragmentComponent
    }
}