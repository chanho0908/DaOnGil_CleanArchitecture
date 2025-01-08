package kr.techit.lion.presentation.main.myinfo.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.techit.lion.domain.exception.onError
import kr.techit.lion.domain.exception.onSuccess
import kr.techit.lion.domain.repository.AuthRepository
import kr.techit.lion.domain.repository.MemberRepository
import kr.techit.lion.presentation.base.BaseViewModel
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityObserver
import kr.techit.lion.presentation.connectivity.connectivity.ConnectivityStatus
import kr.techit.lion.presentation.delegate.NetworkErrorDelegate
import kr.techit.lion.presentation.delegate.NetworkEventDelegate
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.ext.stateInUi
import kr.techit.lion.presentation.main.myinfo.vm.model.ProfileState
import kr.techit.lion.presentation.main.myinfo.vm.model.toUiModel
import kr.techit.lion.presentation.splash.model.LogInStatus
import javax.inject.Inject

@HiltViewModel
class MyInfoMainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val memberRepository: MemberRepository,
    private val networkEventDelegate: NetworkEventDelegate,
    connectivityObserver: ConnectivityObserver
) : BaseViewModel() {

    val networkEvent get() = networkEventDelegate.event

    private val _uiState = MutableStateFlow(ProfileState.create())
    val uiState get() = _uiState.asStateFlow()

    private val _loggedIn = MutableStateFlow<LogInStatus>(LogInStatus.LoginRequired)
    val loggedIn get() = _loggedIn.asStateFlow()

    val connectivityStatus = connectivityObserver.observe()
        .stateInUi(
            scope = viewModelScope,
            initialValue = ConnectivityStatus.Loading
        )

    fun checkLoginState() {
        authRepository
            .loggedIn
            .map { isLoggedIn ->
                if (isLoggedIn) LogInStatus.LoggedIn
                else LogInStatus.LoginRequired
            }.onEach {
                _loggedIn.value = it
            }.launchIn(viewModelScope)
    }

    fun onStateLoggedIn() {
        execute(
            action = { memberRepository.getMyDefaultInfo() },
            eventHandler = networkEventDelegate,
            onSuccess = {
                _uiState.value = _uiState.value.copy(
                    myInfo = it.toUiModel()
                )
            }
        )
    }

    fun logout(action: () -> Unit) {
        viewModelScope.launch {
            authRepository.logout()
            action()
        }
    }
}