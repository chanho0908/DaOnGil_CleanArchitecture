package kr.techit.lion.presentation.concerntype.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.techit.lion.domain.exception.onError
import kr.techit.lion.domain.exception.onSuccess
import kr.techit.lion.domain.repository.MemberRepository
import kr.techit.lion.presentation.concerntype.vm.model.ConcernTypState
import kr.techit.lion.presentation.concerntype.vm.model.ConcernTypes
import kr.techit.lion.presentation.concerntype.vm.model.toUiModel
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityObserver
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityStatus
import kr.techit.lion.presentation.delegate.NetworkErrorDelegate
import kr.techit.lion.presentation.ext.stateInUi
import javax.inject.Inject

@HiltViewModel
class ConcernTypeViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    connectivityObserver: ConnectivityObserver,
): ViewModel() {

    @Inject
    lateinit var networkErrorDelegate: NetworkErrorDelegate
    val networkState get() = networkErrorDelegate.networkState

    val connectivityStatus = connectivityObserver.observe()
        .stateInUi(
            scope = viewModelScope,
            initialValue = ConnectivityStatus.Loading
        )

    private val _uiState = MutableStateFlow(ConcernTypState())
    val uiState = _uiState.asStateFlow()

    fun getConcernType() {
        viewModelScope.launch {
            memberRepository.getConcernType().onSuccess {
                _uiState.value = _uiState.value.copy(
                    savedConcernType = it.toUiModel(),
                    selectedConcernType = it.toUiModel()
                )
                networkErrorDelegate.handleNetworkSuccess()
            }.onError {
                networkErrorDelegate.handleNetworkError(it)
            }
        }
    }

    fun updateConcernType(action: () -> Unit) {
        viewModelScope.launch {
            networkErrorDelegate.handleNetworkLoading()
            memberRepository.updateConcernType(
                _uiState.value.selectedConcernType.toDomainModel()
            ).onSuccess {
                _uiState.value = _uiState.value.copy(
                    savedConcernType = _uiState.value.selectedConcernType
                )
                networkErrorDelegate.handleNetworkSuccess()
                action()
            }.onError {
                networkErrorDelegate.handleNetworkError(it)
            }
        }
    }

    fun onChangeConcernType(selectedType: ConcernTypes){
        if (_uiState.value.selectedConcernType.isSelectedType(selectedType)){
            _uiState.value = _uiState.value.removeSelectedType(selectedType)
        }else{
            _uiState.value = _uiState.value.addSelectedType(selectedType)
        }
    }

    fun setNickName(nickName: String){
        _uiState.value = _uiState.value.copy(
            nickName = nickName
        )
    }
}