package com.jonapoul.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jonapoul.extensions.recyclerview.initialise
import com.jonapoul.extensions.view.hideIfTrue

/**
 * Inflates and initialises a list of [AboutSection]s for display.
 */
internal class SectionsAdapter(
    private val sections: List<AboutSection>
) : RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    /**
     * Inflates the [View]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.about_section, parent, false)
        return ViewHolder(root)
    }

    /**
     * Binds the [ViewHolder]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]
        section.title?.let { holder.headerTextView.setText(it) }
        holder.headerTextView.hideIfTrue(section.title == null)
        holder.itemsRecyclerView.initialise(adapter = ItemsAdapter(section.items))
    }

    /**
     * Number of items in the [RecyclerView]
     */
    override fun getItemCount(): Int = sections.size

    internal class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val headerTextView: TextView = root.findViewById(R.id.section_header)
        val itemsRecyclerView: RecyclerView = root.findViewById(R.id.section_items)
    }
}