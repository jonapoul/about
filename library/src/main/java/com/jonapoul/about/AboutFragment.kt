package com.jonapoul.about

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jonapoul.extensions.recyclerview.initialise

abstract class AboutFragment(
    private val sections: List<AboutSection>
) : Fragment(R.layout.about_layout) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.initialise(adapter = SectionsAdapter(sections))
    }
}
