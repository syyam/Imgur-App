package com.syyamnoor.imgurapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.syyamnoor.imgurapp.R
import com.syyamnoor.imgurapp.databinding.FragmentItemListBinding
import com.syyamnoor.imgurapp.domain.models.Image
import com.syyamnoor.imgurapp.ui.ImageAdapter
import com.syyamnoor.imgurapp.utils.DataState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@AndroidEntryPoint
class ImageListFragment : Fragment() {

    private lateinit var binding: FragmentItemListBinding

    private val imageListViewModel: ImageListViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter
    private var lastBackClick: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemListBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.layoutToolbar.materialToolbar)
        setupDrawerLayout()

        imageAdapter = ImageAdapter { _, image ->
            imageListViewModel.performEvent(
                ImageListViewModel.ListUiEvent.Search(
                    ""
                )
            )
        }

        binding.fabSearch.setOnClickListener {
            findNavController().navigate(
                R.id.show_search_fragment
            )
        }

        binding.recyclerView.adapter = imageAdapter

        binding.swipeRefreshLayout.setOnRefreshListener(this::performQuery)
        binding.layoutFailureState.buttonFailureRetry.setOnClickListener { performQuery() }
        binding.layoutEmptyState.buttonEmptyRetry.setOnClickListener { performQuery() }

        imageListViewModel.listUiState.observe(viewLifecycleOwner) {
            when (val result = it.result) {
                is DataState.Success -> {
                    showSuccess(result)
                }
                is DataState.Failure -> {
                    showFailure(result)
                }
                is DataState.Loading -> {
                    startLoading(result)
                }
            }
        }


        return binding.root

    }

    private fun performQuery() {
        imageListViewModel.performEvent(ImageListViewModel.ListUiEvent.Refresh)
    }

    private fun startLoading(result: DataState.Loading<List<Image>>) {
        val resultValues = result.data

        // If the loading had some data, display the new list, this will take care of
        // hiding and showing empty state for list
        if (resultValues != null) {
            displayList(resultValues)
        }

        binding.linearProgressIndicator.visibility = VISIBLE
        binding.swipeRefreshLayout.isRefreshing = true
        // Prevent calls to refresh while loading
        binding.layoutEmptyState.buttonEmptyRetry.isEnabled = false
        binding.layoutFailureState.buttonFailureRetry.isEnabled = false
        binding.swipeRefreshLayout.isEnabled = false
    }

    private fun stopLoading() {
        binding.linearProgressIndicator.visibility = GONE
        binding.swipeRefreshLayout.isRefreshing = false
        // Re-enable calls to refresh
        binding.layoutEmptyState.buttonEmptyRetry.isEnabled = true
        binding.layoutFailureState.buttonFailureRetry.isEnabled = true
        binding.swipeRefreshLayout.isEnabled = true
    }

    private fun showSuccess(dataState: DataState.Success<List<Image>>) {
        val list = dataState.data
        displayList(list)
        binding.layoutFailureState.root.visibility = GONE
        stopLoading()
    }

    private fun showFailure(result: DataState.Failure<List<Image>>) {
        val message = result.throwable.message
        val resultValues = result.data

        // If the failure had some data, display the new list, this will take care of
        // hiding and showing empty state for list
        var itemCount = imageAdapter.itemCount
        if (resultValues != null) {
            displayList(resultValues)
            itemCount = resultValues.size
        }

        // An existing list is being displayed, hide empty state
        if (itemCount > 0) {
            binding.layoutFailureState.textViewFailureMessage.text = message
            binding.layoutEmptyState.root.visibility = GONE
            binding.layoutFailureState.root.visibility = VISIBLE
        }
        // No list to display, show empty state with error message
        else {
            binding.layoutEmptyState.textViewEmptyText.text = message
            binding.layoutEmptyState.root.visibility = VISIBLE
            binding.layoutFailureState.root.visibility = GONE
        }
        stopLoading()
    }

    private fun displayList(list: List<Image>?) {
        imageAdapter.submitList(list)
        if (list == null || list.isEmpty()) {
            binding.recyclerView.visibility = GONE
            binding.layoutEmptyState.root.visibility = VISIBLE
            binding.layoutEmptyState.textViewEmptyText.text =
                getString(R.string.no_image_items_found)
        } else {
            binding.recyclerView.visibility = VISIBLE
            binding.layoutEmptyState.root.visibility = GONE
        }
    }

    private fun setupDrawerLayout() {
        val itemListContainer = binding.itemListContainer

        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when {
                        // Pressed back twice
                        (lastBackClick + BACK_DELAY) > System.currentTimeMillis() -> {
                            activity?.finish()
                        }
                        else -> {
                            Toast.makeText(
                                context,
                                getString(R.string.press_back_again),
                                LENGTH_SHORT
                            ).show()
                            lastBackClick = System.currentTimeMillis()
                        }
                    }
                }
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.main_options_menu, menu)

        val searchItem: MenuItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.queryHint = getString(R.string.search_images)
        searchView.setOnCloseListener {
            imageListViewModel.performEvent(ImageListViewModel.ListUiEvent.Search(null))
            false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                imageListViewModel.performEvent(ImageListViewModel.ListUiEvent.Search(newText))
                return true
            }
        })

        val query = imageListViewModel.listUiState.value?.query
        if (query != null) {
            searchView.isIconified = false
            searchView.setIconifiedByDefault(false)
            searchItem.expandActionView()
            searchView.requestFocus()
            searchView.setQuery(query, false)
        }
    }


    companion object {
        const val BACK_DELAY = 2000L
    }
}