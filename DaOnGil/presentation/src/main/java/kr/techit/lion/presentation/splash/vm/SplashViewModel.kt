package kr.techit.lion.presentation.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kr.techit.lion.domain.model.Activation
import kr.techit.lion.domain.repository.ActivationRepository
import kr.techit.lion.domain.usecase.areacode.InitAreaCodeInfoUseCase
import kr.techit.lion.domain.usecase.base.onError
import kr.techit.lion.domain.usecase.base.onSuccess
import kr.techit.lion.presentation.ext.stateInUi
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val activationRepository: ActivationRepository,
    private val initAreaCodeInfoUseCase: InitAreaCodeInfoUseCase,
): ViewModel() {

    private val _errorState = MutableStateFlow(false)
    val errorState get() = _errorState.asStateFlow()

    val userActivationState = activationRepository
        .activation
        .stateInUi(scope = viewModelScope, initialValue = Activation.Loading)

    suspend fun whenUserActivationIsDeActivate(onComplete: () -> Unit){
        initAreaCodeInfoUseCase().onSuccess {
            onComplete()
        }.onError {
            _errorState.value = true
        }
    }
}