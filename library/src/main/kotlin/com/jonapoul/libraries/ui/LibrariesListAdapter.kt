package com.jonapoul.libraries.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.jonapoul.about.databinding.FragmentLibrariesItemBinding
import com.jonapoul.common.data.replaceAllWith
import com.jonapoul.libraries.data.db.LibraryEntity

internal class LibrariesListAdapter(
    private val onLaunchUrl: (url: String?) -> Unit,
) : ListAdapter<LibraryEntity, LibraryViewHolder>(DIFF_CALLBACK),
    Filterable {

    private val libraries = arrayListOf<LibraryEntity>()
    private val filteredLibraries = arrayListOf<LibraryEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FragmentLibrariesItemBinding.inflate(inflater, parent, false)
        return LibraryViewHolder(binding, onLaunchUrl)
    }

    override fun onBindViewHolder(holder: LibraryViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    fun setData(newLibraries: MutableList<LibraryEntity>) {
        libraries.replaceAllWith(newLibraries)
        submitList(newLibraries)
    }

    override fun submitList(list: MutableList<LibraryEntity>?) {
        super.submitList(list)
        this.filteredLibraries.replaceAllWith(list ?: emptyList())
    }

    override fun getItemViewType(position: Int): Int = position

    override fun getFilter(): Filter = filter

    private val filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filtered = arrayListOf<LibraryEntity>()
            if (constraint == null || constraint.isEmpty()) {
                filtered.addAll(libraries)
            } else {
                val pattern = constraint.toString().lowercase().trim()
                filtered.addAll(
                    libraries.filter { lib ->
                        lib.project.matches(pattern) or
                            lib.description.matches(pattern) or
                            lib.authors.matches(pattern) or
                            lib.license.matches(pattern) or
                            lib.version.matches(pattern)
                    }
                )
            }
            return FilterResults().apply { values = filtered }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            submitList(results.values as MutableList<LibraryEntity>)
        }

        private fun String?.matches(pattern: String): Boolean =
            this?.contains(pattern, ignoreCase = true) ?: false
    }

    private companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LibraryEntity>() {
            override fun areItemsTheSame(oldItem: LibraryEntity, newItem: LibraryEntity): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: LibraryEntity, newItem: LibraryEntity): Boolean =
                oldItem.artifact == newItem.artifact
        }
    }
}
