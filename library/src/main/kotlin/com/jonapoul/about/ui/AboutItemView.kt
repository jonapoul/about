package com.jonapoul.about.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import com.jonapoul.about.R
import com.jonapoul.about.databinding.ViewAboutItemBinding

internal class AboutItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(
    context,
    attrs,
    defStyleAttr,
    defStyleRes
) {
    private val binding = ViewAboutItemBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    init {
        context.withStyledAttributes(attrs, R.styleable.AboutItemView) {
            val icon = getDrawable(R.styleable.AboutItemView_about_icon)
                ?: error("Required value for app:about_icon!")
            binding.itemIcon.setImageDrawable(icon)

            val title = getString(R.styleable.AboutItemView_about_title)
                ?: error("Required value for app:about_title!")
            binding.itemTitle.text = title

            getString(R.styleable.AboutItemView_about_subtitle)?.let {
                setSubtitle(it)
            }
        }
    }

    fun setSubtitle(subtitle: String) {
        binding.itemSubtitle.text = subtitle
    }
}
