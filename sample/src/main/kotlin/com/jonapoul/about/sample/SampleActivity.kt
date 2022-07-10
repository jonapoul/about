package com.jonapoul.about.sample

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupActionBarWithNavController
import com.jonapoul.about.sample.databinding.ActivitySampleBinding
import com.jonapoul.common.ui.SnackbarMessage
import com.jonapoul.common.ui.collectFlow
import com.jonapoul.common.ui.navControllers
import com.jonapoul.common.ui.notifier.Notifier
import com.jonapoul.common.ui.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SampleActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivitySampleBinding::inflate)
    private val viewModel by viewModels<SampleActivityViewModel>()
    private val navController by navControllers(R.id.nav_host_fragment)

    private val notifier = Notifier(
        backgroundColour = com.jonapoul.common.ui.R.color.snackbarBackground,
        successColour = com.jonapoul.common.ui.R.color.onSnackbarGreen,
        infoColour = com.jonapoul.common.ui.R.color.onSnackbarBlue,
        cautionColour = com.jonapoul.common.ui.R.color.onSnackbarYellow,
        warningColour = com.jonapoul.common.ui.R.color.onSnackbarRed,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("onCreate")
        super.onCreate(savedInstanceState)

        /* Set the view and initialise navigation via the app bar */
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)

        /* Receive and display any snackbar messages */
        collectFlow(viewModel.snackbars, ::onSnackbar)
    }

    private fun onSnackbar(snackbar: SnackbarMessage?) {
        Timber.d("handleSnackbar $snackbar")
        snackbar?.notify?.invoke(notifier, binding.root)
    }

    override fun onSupportNavigateUp(): Boolean {
        Timber.d("onSupportNavigateUp")
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
