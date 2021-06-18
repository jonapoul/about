package com.jonapoul.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jonapoul.extensions.hideIfTrue
import com.jonapoul.extensions.setLifecycleAwareAdapter

internal class SectionsAdapter(
    private val sections: List<AboutSection>
) : RecyclerView.Adapter<SectionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.about_section, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val section = sections[position]
        val context = holder.headerTextView.context
        section.title?.let { holder.headerTextView.setText(it) }
        holder.headerTextView.hideIfTrue(section.title == null)
        holder.itemsRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.itemsRecyclerView.setLifecycleAwareAdapter(ItemsAdapter(section.items))
    }

    override fun getItemCount(): Int = sections.size

    class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val headerTextView: TextView = root.findViewById(R.id.section_header)
        val itemsRecyclerView: RecyclerView = root.findViewById(R.id.section_items)
    }
}
