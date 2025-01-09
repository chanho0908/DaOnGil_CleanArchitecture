package kr.techit.lion.presentation.keyword.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.databinding.FragmentSearchResultBinding
import kr.techit.lion.presentation.ext.addOnScrollEndListener
import kr.techit.lion.presentation.ext.hideSoftInput
import kr.techit.lion.presentation.ext.repeatOnViewStarted
import kr.techit.lion.presentation.home.DetailActivity
import kr.techit.lion.presentation.keyword.adapter.SearchResultAdapter
import kr.techit.lion.presentation.keyword.vm.SearchResultViewModel
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityStatus
import kr.techit.lion.presentation.delegate.NetworkEvent

@AndroidEntryPoint
class SearchResultFragment : Fragment(R.layout.fragment_search_result) {
    private val viewModel: SearchResultViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchResultBinding.bind(view)
        val progressBar = requireActivity().findViewById<ProgressBar>(R.id.search_view_progressBar)
        val searchText = arguments?.getString("searchText") ?: ""

        requireContext().hideSoftInput(binding.noSearchResultContainer)
        requireActivity().findViewById<TextInputEditText>(R.id.search_edit).setText(searchText)

        val rvAdapter = SearchResultAdapter {
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("detailPlaceId", it)
            startActivity(intent)
        }

        with(binding.searchResultRv) {
            adapter = rvAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            addOnScrollEndListener {
                val pageState = viewModel.uiState.value.isLastPage
                if (pageState.not()) {
                    viewModel.loadPlace(searchText)
                }
            }

            repeatOnViewStarted {
                launch {
                    viewModel.connectivityStatus.collect { status ->
                        when (status) {
                            ConnectivityStatus.Loading -> Unit
                            ConnectivityStatus.Available -> {
                                viewModel.loadPlace(searchText)
                            }
                            is ConnectivityStatus.OnLost -> {
                                showNetworkErrorPage(
                                    binding,
                                    progressBar,
                                    requireContext().getString(R.string.can_not_access_network)
                                )
                            }
                        }
                    }
                }

                launch {
                    combine(viewModel.networkEvent, viewModel.uiState) { event, uiState ->
                        when (event) {
                            is NetworkEvent.Loading -> {
                                progressBar.visibility = View.VISIBLE
                            }

                            is NetworkEvent.Success -> {
                                val place = uiState.place
                                progressBar.visibility = View.GONE
                                if (place.isEmpty()) {
                                    binding.noSearchResultContainer.visibility = View.VISIBLE
                                    binding.searchResultRv.visibility = View.GONE
                                } else {
                                    binding.noSearchResultContainer.visibility = View.GONE
                                    binding.searchResultRv.visibility = View.VISIBLE
                                    rvAdapter.submitList(place)
                                }
                            }

                            is NetworkEvent.Error -> {
                                showNetworkErrorPage(binding, progressBar, event.msg)
                            }
                        }
                    }.collect { }
                }
            }
        }
    }

    private fun showNetworkErrorPage(
        binding: FragmentSearchResultBinding,
        progressBar: ProgressBar,
        msg: String
    ) {
        with(binding) {
            searchResultRv.visibility = View.GONE
            progressBar.visibility = View.GONE
            noSearchResultContainer.visibility = View.VISIBLE
            textMsg.text = msg
        }
    }
}