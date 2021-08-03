/*
 * *
 *  * Created by estiven on 3/08/21, 3:05 p. m.
 *  * Copyright (c) 2021 . All rights reserved.
 *  * Last modified 20/07/21, 11:17 p. m.
 *
 */

package com.alp.app.ui.main.view.activities

/**
 * extracted from de https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample
 */

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.alp.app.R
import com.alp.app.singleton.PreferencesSingleton
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        PreferencesSingleton.init(this, resources.getString(R.string.name_preferences))
        val mode = PreferencesSingleton.read("mode_dark", false)
        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }
        if (mode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // Now that BottomNavigationBar has restored its instance state
        // and its selectedItemId, we can proceed with setting up the
        // BottomNavigationBar with Navigation
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navGraphIds = mutableListOf(R.navigation.navigation_code, R.navigation.navigation_dashboard, R.navigation.navigation_profile)

        //Configuring the bottom navigation view with a list of navigation charts
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )
        bottomNavigationView.selectedItemId = R.id.home
        //Whenever you change the selected controller, configure the action bar.
        controller.observe(this) { navController ->
            setupActionBarWithNavController(navController)
        }
        currentNavController = controller
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}