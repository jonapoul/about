package com.jonapoul.about

import android.content.Context
import androidx.appcompat.app.AlertDialog

/**
 * Offers a separate interface for constructing an [AboutDialogBuilder]. This is a functional
 * method, as opposed to the builder pattern from [AboutDialogBuilder].
 */
object AboutDialog {
    /**
     * Offers a separate interface for constructing an [AboutDialogBuilder]. Used as:
     *
     *      val dialog = AboutDialog.show(
     *          context,
     *          useDefaults = true,
     *          sections = listOf(
     *              AboutSection(),
     *              AboutSection()
     *          ),
     *          extraConfig = {
     *              setNegativeButton("No", null)
     *              setIcon(R.drawable.ic_picture)
     *          }
     *      )
     */
    fun show(
        context: Context,
        sections: List<AboutSection>,
        useDefaults: Boolean = false,
        extraConfig: AboutDialogBuilder.() -> AboutDialogBuilder = { this }
    ): AlertDialog = AboutDialogBuilder(context, useDefaults)
        .setSections(sections)
        .extraConfig()
        .show()
}
