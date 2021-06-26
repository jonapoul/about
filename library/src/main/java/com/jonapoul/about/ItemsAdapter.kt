package com.jonapoul.about

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jonapoul.extensions.view.hide
import com.jonapoul.extensions.view.show

internal class ItemsAdapter(
    private val items: List<AboutItem>
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val root = inflater.inflate(R.layout.about_item, parent, false)
        return ViewHolder(root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.icon)
        holder.title.text = item.title
        holder.subtitle.text = item.subtitle
        if (item.onClickButton != null) {
            holder.launchButton.show()
            holder.itemView.setOnClickListener {
                item.onClickButton.onItemClick(holder.itemView.context)
            }
        } else {
            holder.launchButton.hide()
        }
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val icon: ImageView = root.findViewById(R.id.item_icon)
        val title: TextView = root.findViewById(R.id.item_title)
        val subtitle: TextView = root.findViewById(R.id.item_subtitle)
        val launchButton: ImageView = root.findViewById(R.id.item_button)
    }
}
