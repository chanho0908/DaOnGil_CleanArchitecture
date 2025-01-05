package kr.techit.lion.presentation.keyword.vm

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import kr.techit.lion.domain.exception.NetworkError
import kr.techit.lion.domain.exception.TimeoutError
import kr.techit.lion.domain.exception.UnknownError
import kr.techit.lion.domain.exception.UnknownHostError
import kr.techit.lion.domain.model.search.AutoCompleteKeyword
import kr.techit.lion.domain.model.search.toRecentlySearchKeyword
import kr.techit.lion.domain.repository.PlaceRepository
import kr.techit.lion.domain.repository.RecentlySearchKeywordRepository
import kr.techit.lion.presentation.base.BaseViewModel
import kr.techit.lion.presentation.delegate.NetworkErrorDelegate
import kr.techit.lion.presentation.keyword.vm.model.KeywordInputStatus
import kr.techit.lion.presentation.keyword.vm.model.KeywordSearchState
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityObserver
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityStatus
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.ext.stateInUi
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

@HiltViewModel
class KeywordSearchViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val recentlySearchKeywordRepository: RecentlySearchKeywordRepository,
    connectivityObserver: ConnectivityObserver
) : BaseViewModel() {

    @Inject
    lateinit var networkErrorDelegate: NetworkErrorDelegate
    val errorState get() = networkErrorDelegate.networkState

    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

    private val _uiState = MutableStateFlow(KeywordSearchState())
    val uiState = _uiState.asStateFlow()

    val connectivityStatus = connectivityObserver.observe()
        .stateInUi(
            scope = viewModelScope,
            initialValue = ConnectivityStatus.Loading
        )

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val autocompleteKeyword = combine(
        _keyword,
        connectivityStatus,
        uiState.map { it.inputStatus }
    ) { keyword, networkStatus, keywordInputStatus ->
        Triple(keyword, networkStatus, keywordInputStatus)
    }.filter { (keyword, status, inputStatus) ->
        keyword.isNotEmpty() &&
                status == ConnectivityStatus.Available &&
                inputStatus != KeywordInputStatus.Erasing
    }.map { (keyword, _, _) ->
        keyword
    }.debounce(DEBOUNCE_INTERVAL)
    .distinctUntilChanged()
    .flatMapLatest { keyword ->
        val response = placeRepository.getAutoCompleteKeyword(keyword)
        networkErrorDelegate.handleNetworkSuccess()
        response
    }
    .flowOn(recordExceptionHandler)
    .catch { e ->
        submitThrowableState(e)
    }

    fun inputTextChanged(keyword: String) {
        if (_uiState.value.inputStatus != KeywordInputStatus.Erasing) {
            _keyword.value = keyword
        }
    }

    fun keywordInputStateChanged(status: KeywordInputStatus) {
        _uiState.value = _uiState.value.copy(inputStatus = status)
    }

    fun loadSavedKeyword() {
        recentlySearchKeywordRepository
            .savedKeyword
            .onEach {
                _uiState.value = _uiState.value.copy(
                    keywordList = it
                )
            }.launchIn(viewModelScope)
    }

    fun insertKeyword(keyword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val existingKeyword = _uiState.value.keywordList.isExistKeyword(keyword)
            if (existingKeyword) {
                val findKeyword = _uiState.value.keywordList.findKeyword(keyword)
                recentlySearchKeywordRepository.deleteKeyword(findKeyword)
            }
            recentlySearchKeywordRepository.insertKeyword(keyword)
        }
    }

    fun deleteKeyword(id: Long) {
        viewModelScope.launch {
            recentlySearchKeywordRepository.deleteKeyword(id)
        }
    }

    fun deleteAllKeyword() {
        viewModelScope.launch {
            recentlySearchKeywordRepository.deleteAllKeyword()
        }
    }

    private fun submitThrowableState(e: Throwable) {
        when (e) {
            is TimeoutException -> {
                networkErrorDelegate.handleNetworkError(TimeoutError)
            }

            is UnknownHostException -> {
                networkErrorDelegate.handleNetworkError(UnknownHostError)
            }

            is UnknownError -> {
                networkErrorDelegate.handleNetworkError(UnknownError)
            }

            else -> {
                networkErrorDelegate.handleNetworkError(e as NetworkError)
            }
        }
    }

    companion object {
        private const val DEBOUNCE_INTERVAL = 300L
    }
}