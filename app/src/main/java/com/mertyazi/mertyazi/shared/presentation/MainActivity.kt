package com.mertyazi.mertyazi.shared.presentation

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.mertyazi.mertyazi.R
import com.mertyazi.mertyazi.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setupNavView()
        } else {
            setupBottomNavView()
        }
    }

    private fun setupNavView() {
        val navView: NavigationView = binding.navView!!
        navView.itemIconTintList = null
        mNavController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.moviesFragment,
                R.id.favoritesFragment
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        navView.setupWithNavController(mNavController)
        supportActionBar?.hide()

        navView.setNavigationItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, mNavController)
            mNavController.popBackStack(item.itemId, inclusive = false)
            true
        }
    }

    private fun setupBottomNavView() {
        val bottomNavView: BottomNavigationView = binding.bottomNavView!!
        mNavController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.moviesFragment,
                R.id.favoritesFragment
            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        bottomNavView.setupWithNavController(mNavController)
        supportActionBar?.hide()

        bottomNavView.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, mNavController)
            mNavController.popBackStack(item.itemId, inclusive = false)
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(mNavController, null) || super.onSupportNavigateUp()
    }
}