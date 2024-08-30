package kr.tekit.lion.presentation.keyword.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.tekit.lion.domain.exception.NetworkError
import kr.tekit.lion.domain.exception.onError
import kr.tekit.lion.domain.exception.onSuccess
import kr.tekit.lion.domain.model.search.RecentlySearchKeyword
import kr.tekit.lion.domain.model.search.toRecentlySearchKeyword
import kr.tekit.lion.domain.repository.PlaceRepository
import kr.tekit.lion.domain.repository.RecentlySearchKeywordRepository
import kr.tekit.lion.presentation.delegate.NetworkErrorDelegate
import kr.tekit.lion.presentation.keyword.model.KeywordSearch
import kr.tekit.lion.presentation.main.model.PlaceModel
import javax.inject.Inject

@HiltViewModel
class KeywordSearchViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val recentlySearchKeywordRepository: RecentlySearchKeywordRepository
) : ViewModel() {

    init {
        viewModelScope.launch {
            loadSavedKeyword()
        }
    }

    @Inject
    lateinit var networkErrorDelegate: NetworkErrorDelegate

    val errorMessage: StateFlow<String?> get() = networkErrorDelegate.errorMessage

    private val _place: MutableStateFlow<PlaceModel> = MutableStateFlow(PlaceModel())
    val place: StateFlow<PlaceModel> = _place.asStateFlow()

    private val _recentlySearchKeyword = MutableStateFlow<List<RecentlySearchKeyword>>(emptyList())
    val recentlySearchKeyword = _recentlySearchKeyword.asStateFlow()

    private val _keyword = MutableStateFlow("")
    val keyword = _keyword.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val autocompleteKeyword = keyword
        .filter { it.isNotBlank() }
        .debounce(DEBOUNCE_INTERVAL)
        .flatMapLatest { keyword ->
            placeRepository.getAutoCompleteKeyword(keyword)
        }
        .flowOn(Dispatchers.IO)
        .catch { e: Throwable ->
            when (e) {
                is NetworkError -> networkErrorDelegate.handleNetworkError(e)
            }
        }

    fun updateKeyword(keyword: String) {
        _keyword.value = keyword
    }

    fun onClickSearchButton(keyword: String) = viewModelScope.launch(Dispatchers.IO){
        placeRepository.getSearchPlaceResultByList(
            KeywordSearch(keyword = keyword, page = 0).toDomainModel()
        ).onSuccess {
            _place.update { it }
        }.onError {
            networkErrorDelegate.handleNetworkError(it)
        }
    }

    private fun loadSavedKeyword() = viewModelScope.launch(Dispatchers.IO){
        recentlySearchKeywordRepository.readAllKeyword().collect{
            _recentlySearchKeyword.value =  it
        }
    }

    fun insertKeyword(keyword: String) = viewModelScope.launch(Dispatchers.IO) {
        val existingKeyword = _recentlySearchKeyword.value.firstOrNull { it.keyword == keyword }
        if (existingKeyword != null) {
            existingKeyword.id?.let { recentlySearchKeywordRepository.deleteKeyword(it) }
        }
        recentlySearchKeywordRepository.insertKeyword(keyword.toRecentlySearchKeyword())
    }

    fun deleteKeyword(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        recentlySearchKeywordRepository.deleteKeyword(id)
    }

    fun deleteAllKeyword() = viewModelScope.launch(Dispatchers.IO) {
        recentlySearchKeywordRepository.deleteAllKeyword()
    }

    companion object {
        private const val DEBOUNCE_INTERVAL = 300L
    }
}