package kr.techit.lion.presentation.myinfo.vm

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kr.techit.lion.domain.exception.onError
import kr.techit.lion.domain.exception.onSuccess
import kr.techit.lion.domain.repository.AuthRepository
import kr.techit.lion.presentation.base.BaseViewModel
import kr.techit.lion.presentation.delegate.NetworkErrorDelegate
import kr.techit.lion.presentation.delegate.NetworkEventDelegate
import javax.inject.Inject

@HiltViewModel
class DeleteUserViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val networkEventDelegate: NetworkEventDelegate
): BaseViewModel() {

    @Inject
    lateinit var networkErrorDelegate: NetworkErrorDelegate

    val networkEvent get() = networkEventDelegate.event

    fun withdrawal(action: () -> Unit){
        execute(
            action = { authRepository.withdraw() },
            eventHandler = networkEventDelegate,
            onSuccess = {
                action()
            }
        )
    }
}