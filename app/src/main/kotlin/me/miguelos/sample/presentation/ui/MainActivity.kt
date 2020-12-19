package me.miguelos.sample.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import me.miguelos.sample.R
import me.miguelos.sample.databinding.ActivityMainBinding
import me.miguelos.sample.presentation.core.ToolbarTitleListener
import me.miguelos.sample.util.NetworkConnectionMonitor
import me.miguelos.sample.util.showSnackbar


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ToolbarTitleListener {

    private var binding: ActivityMainBinding? = null
    private var connectionMonitor: NetworkConnectionMonitor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController: NavController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding?.toolbar?.setupWithNavController(navController, appBarConfiguration)

        initNetworkManager()
    }

    private fun initNetworkManager() {
        connectionMonitor = NetworkConnectionMonitor(applicationContext).apply {
            observe(this@MainActivity) {
                handleConnectionState(it)
            }
        }
    }

    private fun handleConnectionState(connected: Boolean) {
        if (!connected) {
            binding?.mainLl?.showSnackbar(getString(R.string.dialog_offline))
        }
    }

    fun showDialog(message: String) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .setNegativeButton(R.string.close_button) { dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    override fun onPause() {
        connectionMonitor?.unregisterDefaultNetworkCallback()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        connectionMonitor?.registerDefaultNetworkCallback()
    }

    override fun updateTitle(title: String) {
        binding?.toolbar?.title = title
    }

    companion object {
        const val ARG_ID = "character_id"
    }
}
