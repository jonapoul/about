package com.jonapoul.libraries.domain

import android.content.Context
import com.jonapoul.about.R
import com.jonapoul.common.domain.TextCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class LibrariesTextCreator @Inject constructor(
    @ApplicationContext context: Context,
) : TextCreator(context) {

    fun failedLoadingLibraries(message: String): String =
        context.getString(R.string.library_loading_failed, message)

    val noLibrariesFound: String =
        fromRes(R.string.library_none_found)

    val unknown: String =
        fromRes(R.string.library_unknown)

    val noBrowserFound: String =
        fromRes(R.string.about_no_browser_found)
}
