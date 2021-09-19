package com.gno.erbs.erbs.stats.di.module

import android.content.Context
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.di.ActivityContext
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.ui.LoadingImageHelper
import com.gno.erbs.erbs.stats.ui.guide.characters.CharactersAdapter
import com.gno.erbs.erbs.stats.ui.top.RankAdapter
import dagger.Module
import dagger.Provides
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@Module
class FragmentModule {

    @Provides
    fun provideRankAdapter(
        fragment: Fragment,
        navigateHelper: NavigateHelper,
        loadingImageHelper: LoadingImageHelper
    ): RankAdapter = RankAdapter(loadingImageHelper) { code, name ->
        val bundle = bundleOf("code" to code, "name" to name)
        navigateHelper.go(fragment.findNavController(), R.id.nav_user_stats, bundle)
    }

    @Provides
    fun provideCharactersAdapter(
        fragment: Fragment,
        navigateHelper: NavigateHelper,
        loadingImageHelper: LoadingImageHelper
    ): CharactersAdapter = CharactersAdapter(loadingImageHelper) { code, name ->
        val bundle = bundleOf("code" to code, "name" to name)
        navigateHelper.go(fragment.findNavController(), R.id.nav_character_detail, bundle)
    }

}



