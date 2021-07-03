package com.jonapoul.about

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jonapoul.extensions.view.hide
import com.jonapoul.extensions.view.show

/**
 * Inflates and initialises a list of [AboutItem]s for display.
 */
internal class ItemsAdapter(
    private val items: List<AboutItem>
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    /**
     * Inflates the [View]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.about_item, parent, false)
        return ViewHolder(root)
    }

    /**
     * Binds the [ViewHolder]
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.icon)
        holder.title.text = item.title
        holder.subtitle.text = item.subtitle
        if (item.onClick != null) {
            holder.launchButton.show()
            val context = holder.itemView.context
            holder.itemView.setOnClickListener { item.onClick.onItemClick(context) }
            holder.launchButton.setOnClickListener { item.onClick.onItemClick(context) }

            /* Attach a ripple effect to the item */
            val outValue = TypedValue()
            holder.itemView.context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                outValue,
                true
            )
            holder.itemView.setBackgroundResource(outValue.resourceId)
        } else {
            /* Remove the launcher button if no click listener is supplied */
            holder.launchButton.hide()
        }
    }

    /**
     * Number of items in the [RecyclerView]
     */
    override fun getItemCount(): Int = items.size

    internal class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val icon: ImageView = root.findViewById(R.id.item_icon)
        val title: TextView = root.findViewById(R.id.item_title)
        val subtitle: TextView = root.findViewById(R.id.item_subtitle)
        val launchButton: ImageView = root.findViewById(R.id.item_button)
    }
}
