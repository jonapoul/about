package com.jonapoul.libraries.ui

import androidx.recyclerview.widget.RecyclerView
import com.jonapoul.about.databinding.FragmentLibrariesItemBinding
import com.jonapoul.common.ui.view.showIfTrue
import com.jonapoul.libraries.data.db.LibraryEntity

internal class LibraryViewHolder(
    private val binding: FragmentLibrariesItemBinding,
    private val onLaunchUrl: (url: String?) -> Unit,
) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(item: LibraryEntity) = with(binding) {
        /* Set the text fields */
        libraryTitle.text = item.project
        libraryVersion.text = item.version
        libraryArtifact.text = item.artifact
        libraryAuthors.text = item.authors
        libraryLicense.text = item.license
        libraryDescription.text = item.description

        /* Launch webpage when clicking an item, if there's a URL. Hide the launch button if not and
        * disable clicking. */
        val isClickable = item.url != null
        if (isClickable) {
            root.setOnClickListener {
                onLaunchUrl.invoke(item.url)
            }
        }
        root.isClickable = isClickable
        launchButton.showIfTrue(isClickable)
    }
}
