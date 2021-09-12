package com.gno.erbs.erbs.stats

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.customview.widget.Openable
import androidx.navigation.findNavController
import com.gno.erbs.erbs.stats.databinding.ActivityMainBinding
import com.gno.erbs.erbs.stats.repository.DataRepository
import com.gno.erbs.erbs.stats.repository.NavigateHelper
import com.gno.erbs.erbs.stats.repository.NavigationHistory
import com.gno.erbs.erbs.stats.ui.ErrorHelper
import com.gno.erbs.erbs.stats.ui.search.SearchDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding

    var navigationHistories: List<NavigationHistory>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //////////////////////////

        val appSharedPreferences = getSharedPreferences("APP", MODE_PRIVATE)

        if (appSharedPreferences.getBoolean("FIRST_LAUNCH", true)) {
            DataRepository.setDefaultValues(applicationContext)
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
                NavigateHelper.go(navController, it.navigateId, it.bundle)
            } ?: navController.navigate(menuItem.itemId)
            (binding.navView.parent as Openable).close()
            true
        }

        val menu: Menu = binding.navView.menu
        val submenu = menu.addSubMenu("History")

        DataRepository.navigationHistoryLiveData?.observe(this) { navigationHistories ->
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
        ErrorHelper.showConnectionError(binding.drawerLayout, function)
    }

}