package kr.techit.lion.presentation.keyword.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.techit.lion.domain.model.search.AutoCompleteKeyword
import kr.techit.lion.domain.model.search.RecentlySearchKeyword
import kr.techit.lion.presentation.R
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityStatus
import kr.techit.lion.presentation.databinding.FragmentOnSearchBinding
import kr.techit.lion.presentation.delegate.NetworkEvent
import kr.techit.lion.presentation.ext.repeatOnViewStarted
import kr.techit.lion.presentation.home.DetailActivity
import kr.techit.lion.presentation.keyword.adapter.SearchSuggestionsAdapter
import kr.techit.lion.presentation.keyword.vm.KeywordSearchViewModel
import kr.techit.lion.presentation.keyword.vm.model.KeywordInputStatus
import kr.techit.lion.presentation.main.adapter.RecentlyKeywordAdapter
import kr.techit.lion.presentation.main.dialog.ConfirmDialog

@AndroidEntryPoint
class OnSearchFragment : Fragment(R.layout.fragment_on_search) {
    private val viewModel: KeywordSearchViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentOnSearchBinding.bind(view)
        viewModel.loadSavedKeyword()

        val recentlyKeywordAdapter = RecentlyKeywordAdapter(
            onClick = {
                findNavController().navigate(
                    R.id.action_to_searchResultFragment,
                    bundleOf("searchText" to it)
                )
            },
            onClickDeleteBtn = { keywordId ->
                keywordId?.let { viewModel.deleteKeyword(it) }
            }
        )

        val searchAdapter = SearchSuggestionsAdapter { keyword ->
            viewModel.insertKeyword(keyword.placeName)
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("detailPlaceId", keyword.placeId)
            startActivity(intent)
        }

        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val keywordLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true)

        with(binding) {
            searchSuggestions.adapter = searchAdapter
            searchSuggestions.layoutManager = layoutManager

            rvRecentSearches.adapter = recentlyKeywordAdapter
            rvRecentSearches.layoutManager = keywordLayoutManager
            (rvRecentSearches.layoutManager as LinearLayoutManager).stackFromEnd = true

            tvDeleteAll.setOnClickListener {
                showDeleteConfirmDialog()
            }

            repeatOnViewStarted {
                launch { collectUiState(binding, recentlyKeywordAdapter) }
                launch { collectAutoCompleteKeyword(binding, searchAdapter) }
                launch { collectNetworkEvent(binding) }
                launch { collectConnectivityStatus(binding) }
            }
        }
    }

    private suspend fun collectUiState(
        binding: FragmentOnSearchBinding,
        recentlyKeywordAdapter: RecentlyKeywordAdapter
    ){
        viewModel.uiState.collect {
            checkSavedKeyword(it.keywordList.value, binding, recentlyKeywordAdapter)
            whenInputChanged(it.inputStatus, binding)
        }
    }

    private suspend fun collectAutoCompleteKeyword(
        binding: FragmentOnSearchBinding,
        searchAdapter: SearchSuggestionsAdapter
    ) {
        viewModel.autocompleteKeyword.collect { suggestKeywords ->
            if (suggestKeywords.isEmpty() &&
                viewModel.uiState.value.inputStatus == KeywordInputStatus.NotEmpty
            ) {
                showNoSearchResultPage(binding)
            } else {
                showAutoCompletePage(binding, searchAdapter, suggestKeywords)
            }
        }
    }

    private suspend fun collectNetworkEvent(binding: FragmentOnSearchBinding){
        viewModel.networkEvent.collect { event ->
            when (event) {
                is NetworkEvent.Loading -> Unit
                is NetworkEvent.Success -> showRecentSearchKeywordPage(binding)
                is NetworkEvent.Error -> showNetworkErrorPage(event.msg, binding)
            }
        }
    }

    private suspend fun collectConnectivityStatus(binding: FragmentOnSearchBinding) {
        viewModel.connectivityStatus.collectLatest { status ->
            when (status) {
                ConnectivityStatus.Loading -> Unit
                ConnectivityStatus.Available -> showRecentSearchKeywordPage(binding)
                is ConnectivityStatus.OnLost -> {
                    showNetworkErrorPage(
                        requireContext().getString(R.string.can_not_access_network),
                        binding
                    )
                }
            }
        }
    }

    private fun whenInputChanged(state: KeywordInputStatus, binding: FragmentOnSearchBinding) {
        when (state) {
            KeywordInputStatus.Initial -> whenInputSearchKeywordIsInitialized(binding)
            KeywordInputStatus.NotEmpty -> whenInputSearchKeywordIsNotEmpty(binding)
            KeywordInputStatus.Empty -> whenInputSearchKeywordIsEmpty()
            KeywordInputStatus.Erasing -> Unit
        }
    }

    private fun checkSavedKeyword(
        keywordList: List<RecentlySearchKeyword>,
        binding: FragmentOnSearchBinding,
        recentlyKeywordAdapter: RecentlyKeywordAdapter
    ) {
        with(binding) {
            if (keywordList.isEmpty()) {
                rvRecentSearches.visibility = View.GONE
                tvNoSearch.visibility = View.VISIBLE
            } else {
                rvRecentSearches.visibility = View.VISIBLE
                tvNoSearch.visibility = View.GONE
                recentlyKeywordAdapter.submitList(keywordList)
            }
        }
    }

    private fun whenInputSearchKeywordIsInitialized(binding: FragmentOnSearchBinding) {
        with(binding) {
            searchRecentSearchesContainer.visibility = View.VISIBLE
            searchSuggestions.visibility = View.GONE
            noSearchResultContainer.visibility = View.GONE
        }
    }

    private fun whenInputSearchKeywordIsEmpty() {
        viewModel.keywordInputStateChanged(KeywordInputStatus.Initial)
    }

    private fun whenInputSearchKeywordIsNotEmpty(binding: FragmentOnSearchBinding) {
        binding.searchRecentSearchesContainer.visibility = View.GONE
    }

    private fun showRecentSearchKeywordPage(binding: FragmentOnSearchBinding) {
        with(binding) {
            when (viewModel.uiState.value.inputStatus) {
                KeywordInputStatus.Empty,
                KeywordInputStatus.Initial -> showResult(binding)
                KeywordInputStatus.NotEmpty -> {
                    searchRecentSearchesContainer.visibility = View.GONE
                    showResult(binding)
                }
                KeywordInputStatus.Erasing -> Unit
            }
        }
    }

    private fun showAutoCompletePage(
        binding: FragmentOnSearchBinding,
        adapter: SearchSuggestionsAdapter,
        suggestKeywords: List<AutoCompleteKeyword>
    ) {
        showResult(binding)
        adapter.submitList(suggestKeywords)
    }

    private fun showNoSearchResultPage(binding: FragmentOnSearchBinding) {
        with(binding) {
            textMsg.text = requireContext().getString(R.string.text_no_search_result)
            hideResult(binding)
        }
    }

    private fun showNetworkErrorPage(
        errorMsg: String,
        binding: FragmentOnSearchBinding
    ) {
        with(binding) {
            textMsg.text = errorMsg
            searchRecentSearchesContainer.visibility = View.GONE
            hideResult(binding)
        }
    }

    private fun showResult(binding: FragmentOnSearchBinding) {
        with(binding) {
            searchSuggestions.visibility = View.VISIBLE
            noSearchResultContainer.visibility = View.GONE
        }
    }

    private fun hideResult(binding: FragmentOnSearchBinding){
        with(binding) {
            noSearchResultContainer.visibility = View.VISIBLE
            searchSuggestions.visibility = View.GONE
        }
    }

    private fun showDeleteConfirmDialog() {
        val dialog = ConfirmDialog(
            title = requireContext().getString(R.string.text_remove_all_recently_search_keyword),
            subtitle = requireContext().getString(R.string.text_ask_remove_recently_search_keyword),
            posBtnTitle = requireContext().getString(R.string.text_remove_keyword),
        ) {
            viewModel.deleteAllKeyword()
        }
        dialog.isCancelable = false
        dialog.show(childFragmentManager, "showDeleteConfirmDialog")
    }

    override fun onResume() {
        super.onResume()
        viewModel.inputTextChanged("")
    }
}
