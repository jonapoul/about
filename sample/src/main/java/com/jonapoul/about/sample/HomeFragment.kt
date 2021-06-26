package com.jonapoul.about.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.jonapoul.about.AboutDialogBuilder
import com.jonapoul.about.sample.databinding.FragmentHomeBinding
import com.jonapoul.extensions.navigation.safelyNavigate
import com.jonapoul.extensions.sharedprefs.getIntFromPair
import com.jonapoul.extensions.view.hideIfTrue
import com.jonapoul.extensions.view.showIfTrue
import com.jonapoul.extensions.viewbinding.viewBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val prefs by lazy { PreferenceManager.getDefaultSharedPreferences(requireContext()) }
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val navController by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.lightButton.setOnClickListener { changeTheme(MODE_NIGHT_NO) }
        binding.darkButton.setOnClickListener { changeTheme(MODE_NIGHT_YES) }

        val isDark = prefs.getIntFromPair(Prefs.APP_THEME) == MODE_NIGHT_YES
        binding.lightButton.showIfTrue(isDark)
        binding.darkButton.hideIfTrue(isDark)

        binding.dialogButton.setOnClickListener {
            AboutDialogBuilder(requireContext())
                .addSections(ExampleSections.BASIC)
                .useDefaultTitle()
                .useDefaultPositiveButton()
                .show()
        }

        binding.fragmentButton.setOnClickListener {
            navController.safelyNavigate(HomeFragmentDirections.toAbout())
        }
    }

    private fun changeTheme(mode: Int) {
        prefs.edit { putInt(Prefs.APP_THEME.key, mode) }
        setDefaultNightMode(mode)
    }
}
