package com.jonapoul.about.sample

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.jonapoul.about.sample.databinding.FragmentHomeBinding
import com.jonapoul.common.domain.safelyNavigate
import com.jonapoul.common.ui.CommonFragment
import com.jonapoul.common.ui.viewbinding.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : CommonFragment(layout = R.layout.fragment_home, menu = null) {
    override val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fragmentButton.setOnClickListener {
            navController.navigate(R.id.nav_graph_about)
        }
    }
}
