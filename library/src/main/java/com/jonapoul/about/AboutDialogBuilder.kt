package com.jonapoul.about

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jonapoul.extensions.collections.replaceAllWith
import com.jonapoul.extensions.recyclerview.initialise

/**
 * A class to allow easy construction and display of an [AlertDialog] containing configurable app
 * information. See [AboutDialog.show] as a functional method of constructing these dialogs.
 *
 * Used as below, where `dialog1` and `dialog2` are equivalent:
 *          val sections = listOf(
 *              AboutSection(...),
 *              AboutSection(...)
 *          )
 *          val dialog1 = AboutDialogBuilder(context)
 *              .addSections(sections)
 *              .useDefaultTitle()
 *              .useDefaultPositiveButton()
 *              .show()
 *          val dialog2 = AboutDialogBuilder(context, useDefaults = true)
 *              .addSections(sections)
 *              .show()
 *
 * @param context Ahe current app/activity/fragment [Context].
 * @param useDefaults If true, the dialog will include a default string of "About" as its title and
 *                    a default positive button with no action.
 */
class AboutDialogBuilder(
    context: Context,
    useDefaults: Boolean = false
) : MaterialAlertDialogBuilder(context) {

    private val allSections = arrayListOf<AboutSection>()

    init {
        /* Apply the default values here. Since this is in the init block, the user can override it
        * with any future calls to setTitle or setPositiveButton */
        if (useDefaults) {
            useDefaultTitle()
            useDefaultPositiveButton()
        }
    }

    /**
     * Adds a single [AboutSection] to the dialog. If called multiple times, the sections will be
     * displayed in the order in which they are passed.
     */
    fun addSection(section: AboutSection): AboutDialogBuilder {
        allSections.add(section)
        return this
    }

    /**
     * Adds an arbitrary number of [AboutSection]s to the dialog. This will not replace any sections
     * which may have been added beforehand.
     */
    fun addSections(sections: List<AboutSection>): AboutDialogBuilder {
        allSections.addAll(sections)
        return this
    }

    /**
     * Clears any [AboutSection]s added before and replaces them with the supplied [sections] list.
     */
    fun setSections(sections: List<AboutSection>): AboutDialogBuilder {
        allSections.replaceAllWith(sections)
        return this
    }

    /**
     * Applies a default string of "About" to the dialog's title.
     */
    fun useDefaultTitle(): AboutDialogBuilder {
        setTitle(R.string.about_title)
        return this
    }

    /**
     * Applies a default positive "OK" button to the dialog, which closes the dialog and does
     * nothing else.
     */
    fun useDefaultPositiveButton(): AboutDialogBuilder {
        setPositiveButton(android.R.string.ok, null)
        return this
    }

    /**
     * Builds and displays the [AlertDialog].
     */
    override fun show(): AlertDialog {
        val root = View.inflate(context, R.layout.about_layout, null) as LinearLayout
        val recyclerView = root.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.initialise(adapter = SectionsAdapter(allSections))
        setView(root)
        return super.show()
    }
}
