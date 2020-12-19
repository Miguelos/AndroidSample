package me.miguelos.sample.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.ActivityMainBinding


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToolbarTitleListener {

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding?.toolbar?.setupWithNavController(navController, appBarConfiguration)
    }

    override fun updateTitle(title: String) {
        binding?.toolbar?.title = title
    }

    companion object {
        const val ARG_ID = "character_id"
    }
}

interface ToolbarTitleListener {
    fun updateTitle(title: String)
}
