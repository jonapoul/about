package com.jonapoul.about

import android.content.Context
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jonapoul.extensions.setLifecycleAwareAdapter

class AboutDialogBuilder(context: Context) : MaterialAlertDialogBuilder(context) {

    private val allSections = arrayListOf<AboutSection>()

    fun addSection(section: AboutSection): AboutDialogBuilder {
        allSections.add(section)
        return this
    }

    fun addSections(sections: List<AboutSection>): AboutDialogBuilder {
        allSections.addAll(sections)
        return this
    }

    fun useDefaultTitle(): AboutDialogBuilder {
        setTitle(R.string.lib_name)
        return this
    }

    fun useDefaultPositiveButton(): AboutDialogBuilder {
        setPositiveButton(android.R.string.ok, null)
        return this
    }

    override fun show(): AlertDialog {
        val recyclerView = View.inflate(context, R.layout.about_layout, null) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setLifecycleAwareAdapter(SectionsAdapter(allSections))
        setView(recyclerView)
        return super.show()
    }
}
