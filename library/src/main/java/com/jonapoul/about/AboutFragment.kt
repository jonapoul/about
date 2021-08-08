package com.jonapoul.about

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.jonapoul.extensions.navigation.navControllers
import com.jonapoul.extensions.recyclerview.initialise
import com.jonapoul.extensions.view.hide
import com.jonapoul.extensions.view.show

/**
 * A [Fragment] containing a [RecyclerView] with a list of [AboutSection]s. The
 * [RecyclerView.Adapter] contained within will be automatically cleared to avoid memory leaks.
 *
 * Should be used as:
 *      class MyAboutFragment : AboutFragment(
 *          sections = listOf(
 *              AboutSection(...),
 *              AboutSection(...)
 *          )
 *      )
 * @param sections The list of [AboutSection]s to display under this [Fragment]
 */
abstract class AboutFragment(
    private val sections: List<AboutSection>,
    @StringRes private val footerText: Int? = null
) : Fragment(R.layout.about_layout) {

    protected val navController by navControllers()

    /**
     * Applies the sections to a [RecyclerView].
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.initialise(adapter = SectionsAdapter(sections))

        val footer = view.findViewById<TextView>(R.id.footer_text)
        if (footerText != null) {
            footer.show()
            footer.setText(footerText)
        } else {
            footer.hide()
        }
    }
}
