package com.skash.timetrack

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skash.timetrack.core.helper.sharedprefs.getPrefs
import com.skash.timetrack.core.helper.sharedprefs.saveSelectedWorkspace
import com.skash.timetrack.core.helper.sharedprefs.saveSelfUser
import com.skash.timetrack.core.helper.state.handle
import com.skash.timetrack.core.helper.state.loading.DefaultLoadingDialog
import com.skash.timetrack.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private val viewModel by viewModels<MainViewModel>()

    private val permissionRequestLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { hasPermission ->
        Log.d(javaClass.name, "POST_NOTIFICATIONS permission requested. Accepted? $hasPermission")
    }

    private val loadingDialog by lazy {
        DefaultLoadingDialog(this)
    }

    companion object {
        fun launch(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navView: BottomNavigationView = binding.navView
        navController = findNavHostFragment().navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_overview,
                R.id.navigation_manage,
                R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        requestPermissions()

        viewModel.authenticatedUserLiveData.observe(this) { state ->
            state.handle(this, loadingDialog, onSuccess = {
                getPrefs().saveSelfUser(it)
                it.selectedWorkspace?.let { workspace -> getPrefs().saveSelectedWorkspace(workspace) }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun findNavHostFragment(): NavHostFragment {
        return supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
    }

    private fun requestPermissions() {
        //Request POST_NOTIFICATIONS Permission to start foreground service on android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionRequestLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}