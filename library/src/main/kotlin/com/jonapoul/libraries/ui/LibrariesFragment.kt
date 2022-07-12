package com.jonapoul.libraries.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.jonapoul.about.R
import com.jonapoul.about.databinding.FragmentLibrariesBinding
import com.jonapoul.common.core.exhaustive
import com.jonapoul.common.ui.CommonFragment
import com.jonapoul.common.ui.collectFlow
import com.jonapoul.common.ui.view.hide
import com.jonapoul.common.ui.view.show
import com.jonapoul.common.ui.viewbinding.viewBinding
import com.jonapoul.libraries.data.LibrariesLoadedState
import com.jonapoul.libraries.data.db.LibraryEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class LibrariesFragment : CommonFragment(
    layout = R.layout.fragment_libraries,
    menu = R.menu.menu_libraries
) {
    override val binding by viewBinding(FragmentLibrariesBinding::bind)
    private val viewModel by viewModels<LibrariesViewModel>()

    private var adapter: LibrariesListAdapter? = null
    private var searchButton: MenuItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialiseRecyclerView()
        collectFlow(viewModel.librariesLoadedState, ::onLibrariesLoadedState)
    }

    private fun initialiseRecyclerView() {
        adapter = LibrariesListAdapter(
            onLaunchUrl = viewModel::launchUrl
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateMenu(menu, menuInflater)
        val searchView = menu.findItem(R.id.action_search)
            .also { searchButton = it }
            .actionView as SearchView
        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = false
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter?.filter?.filter(newText)
                return false
            }
        })
    }

    private fun onLibrariesLoadedState(state: LibrariesLoadedState) {
        when (state) {
            LibrariesLoadedState.Loading -> showLoading()
            is LibrariesLoadedState.Success -> showLibraries(state.libraries)
            is LibrariesLoadedState.Failure -> showError(state.message)
        }.exhaustive
    }

    private fun showLoading() {
        searchButton?.isVisible = false
        binding.progressBar.show()
        binding.recyclerView.hide()
        binding.errorText.hide()
    }

    private fun showLibraries(libraries: List<LibraryEntity>) {
        searchButton?.isVisible = true
        binding.progressBar.hide()
        binding.recyclerView.show()
        binding.errorText.hide()
        adapter?.setData(libraries.toMutableList())
    }

    private fun showError(message: String) {
        searchButton?.isVisible = false
        binding.progressBar.hide()
        binding.recyclerView.hide()
        binding.errorText.show()
        binding.errorText.text = message
    }
}
