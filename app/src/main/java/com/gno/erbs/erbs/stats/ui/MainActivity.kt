package com.gno.erbs.erbs.stats.ui

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.Openable
import androidx.navigation.findNavController
import com.gno.erbs.erbs.stats.MainApplication
import com.gno.erbs.erbs.stats.R
import com.gno.erbs.erbs.stats.appComponent
import com.gno.erbs.erbs.stats.databinding.ActivityMainBinding
import com.gno.erbs.erbs.stats.di.component.ActivityComponent
import com.gno.erbs.erbs.stats.di.component.AppComponent
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.repository.NavigationHistory
import com.gno.erbs.erbs.stats.ui.search.SearchDialogFragment
import javax.inject.Inject

class MainActivity : AppCompatActivity() {


    lateinit var _activityComponent: ActivityComponent

    @Inject
    lateinit var binding: ActivityMainBinding
    var navigationHistories: List<NavigationHistory>? = null

    @Inject
    lateinit var dataRepository: DataRepository

    @Inject
    lateinit var navigateHelper: NavigateHelper

    @Inject
    lateinit var errorHelper: ErrorHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityComponent = appComponent.activityComponent().context(this).build()
        _activityComponent.inject(this)

        setContentView(binding.root)

        //////////////////////////

        val appSharedPreferences = getSharedPreferences("APP", MODE_PRIVATE)

        if (appSharedPreferences.getBoolean("FIRST_LAUNCH", true)) {
            dataRepository.setDefaultValues()
            appSharedPreferences.edit().putBoolean("FIRST_LAUNCH", false)
                .apply()
        }

        /////////////////////////////

        binding.appBarMain.fab.setOnClickListener {
            showSearch()
        }

        initDynamicMenu()

    }

    private fun initDynamicMenu() {

        val navController = findNavController(R.id.host)

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            this.navigationHistories?.find { it.name == menuItem.title }?.let {
                navigateHelper.go(navController, it.navigateId, it.bundle)
            } ?: navController.navigate(menuItem.itemId)
            (binding.navView.parent as Openable).close()
            true
        }

        val menu: Menu = binding.navView.menu
        val submenu = menu.addSubMenu("History")

        dataRepository.navigationHistoryLiveData?.observe(this) { navigationHistories ->
            submenu.removeGroup(1)
            this.navigationHistories = navigationHistories
            this.navigationHistories?.forEach {
                submenu.add(1, Menu.NONE, Menu.NONE, it.name)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_view)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun showSearch() {
        val dialog = SearchDialogFragment.newInstance(true)
        dialog.show(supportFragmentManager, "")
    }

    fun searchEnable() {
        binding.appBarMain.fabContainer.visibility = View.VISIBLE
    }

    fun searchDisable() {
        binding.appBarMain.fabContainer.visibility = View.INVISIBLE
    }

    fun showConnectionError(function: () -> Unit) {
        errorHelper.showConnectionError(binding.drawerLayout, function)
    }

}

val Context.activityComponent: ActivityComponent
    get() = when (this) {
        is MainActivity -> _activityComponent
        else -> appComponent.activityComponent().context(this).build()
    }
