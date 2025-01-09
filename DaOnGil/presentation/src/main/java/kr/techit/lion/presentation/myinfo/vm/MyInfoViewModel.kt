package kr.techit.lion.presentation.myinfo.vm

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.techit.lion.domain.exception.NetworkError
import kr.techit.lion.domain.exception.onError
import kr.techit.lion.domain.exception.onSuccess
import kr.techit.lion.domain.model.IceInfo
import kr.techit.lion.domain.model.MyInfo
import kr.techit.lion.domain.model.PersonalInfo
import kr.techit.lion.domain.repository.MemberRepository
import kr.techit.lion.presentation.base.BaseViewModel
import kr.techit.lion.presentation.delegate.NetworkErrorDelegate
import kr.techit.lion.presentation.delegate.NetworkEvent
import kr.techit.lion.presentation.delegate.NetworkEventDelegate
import kr.techit.lion.presentation.delegate.NetworkState
import kr.techit.lion.presentation.myinfo.event.MyInfoEvent
import kr.techit.lion.presentation.myinfo.model.ImgModifyState
import kr.techit.lion.presentation.myinfo.model.MyInfoState
import kr.techit.lion.presentation.myinfo.model.UserProfileImg
import javax.inject.Inject

@HiltViewModel
class MyInfoViewModel @Inject constructor(
    private val memberRepository: MemberRepository,
    private val networkEventDelegate: NetworkEventDelegate
) : BaseViewModel() {

    val networkEvent get() = networkEventDelegate.event

    private val _state = MutableStateFlow(MyInfoState.create())
    val state = _state.asStateFlow()

    fun onChangeUiEvent(event: MyInfoEvent) {
        when (event) {
            is MyInfoEvent.OnUiEventInitializeUiData -> initUiData()
            is MyInfoEvent.OnUiEventModifyPersonalInfo -> onCompleteModifyPersonal(event.personalInfo)
            is MyInfoEvent.OnUiEventModifyIceInfo -> onCompleteModifyIce(event.newIceInfo)
            is MyInfoEvent.OnUiEventSelectProfileImage -> onSelectProfileImage(event.imgUrl)
        }
    }

    private fun initUiData() {
        execute(
            action = { memberRepository.getMyIfo() },
            eventHandler = networkEventDelegate,
            onSuccess = { myInfo ->
                initPersonalInfo(myInfo)
            }
        )
    }

    private fun initPersonalInfo(myInfo: MyInfo) {
        _state.value = _state.value.copy(
            personalInfo = PersonalInfo(
                userName = myInfo.name ?: "",
                nickname = myInfo.nickname ?: "",
                phone = myInfo.phone ?: "",
            ),
            profileImg = UserProfileImg(myInfo.profileImage ?: ""),
            iceInfo = IceInfo(
                bloodType = myInfo.bloodType ?: "",
                birth = myInfo.birth ?: "",
                disease = myInfo.disease ?: "",
                allergy = myInfo.allergy ?: "",
                medication = myInfo.medication ?: "",
                part1Rel = myInfo.part1Rel ?: "",
                part1Phone = myInfo.part1Phone ?: "",
                part2Rel = myInfo.part2Rel ?: "",
                part2Phone = myInfo.part2Phone ?: "",
            )
        )
    }

    private fun onCompleteModifyPersonal(personalInfo: PersonalInfo) {
        when (_state.value.imgSelectedState) {
            ImgModifyState.ImgSelected -> modifyPersonalWithImg(personalInfo)
            ImgModifyState.ImgUnSelected -> modifyPersonal(personalInfo)
        }
    }

    private fun modifyPersonal(personalInfo: PersonalInfo) {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                personalInfo = _state.value.personalInfo.copy(
                    personalInfo.userName, personalInfo.nickname, personalInfo.phone
                ),
                personalModifyNetworkState = NetworkState.Loading
            )
            memberRepository.modifyMyPersonalInfo(_state.value.personalInfo)
                .onSuccess {
                    whenModifyMyInfoSuccess()
                }.onError { e ->
                    whenModifyMyInfoFail(e)
                }
        }
    }

    private fun modifyPersonalWithImg(personalInfo: PersonalInfo) {
        modifyPersonal(personalInfo)
        viewModelScope.launch(recordExceptionHandler) {
            _state.value = _state.value.copy(personalModifyNetworkState = NetworkState.Loading)
            val imgModel = _state.value.profileImg.toDomainModel()
            memberRepository.modifyMyProfileImg(imgModel)
                .onSuccess {
                    whenModifyMyInfoSuccess()
                }.onError { e ->
                    whenModifyMyInfoFail(e)
                }
        }
    }

    private fun whenModifyMyInfoSuccess() {
        _state.value = _state.value.copy(
            isPersonalInfoModified = true,
            personalModifyNetworkState = NetworkState.Success
        )
    }

    private fun whenModifyMyInfoFail(exception: NetworkError) {
        _state.value = _state.value.copy(
            personalModifyNetworkState = NetworkState.Error(
                networkEventDelegate.asUiText(exception)
            )
        )
    }

    private fun onCompleteModifyIce(newIceInfo: IceInfo) {
        viewModelScope.launch(recordExceptionHandler) {
            _state.value = _state.value.copy(
                iceModifyNetworkState = NetworkState.Loading,
                iceInfo = newIceInfo
            )
            memberRepository.modifyMyIceInfo(newIceInfo)
                .onSuccess {
                    _state.value = _state.value.copy(iceModifyNetworkState = NetworkState.Success)
                }.onError { e ->
                    _state.value = _state.value.copy(
                        iceModifyNetworkState = NetworkState.Error(networkEventDelegate.asUiText(e))
                    )
                }
        }
    }

    private fun onSelectProfileImage(imgUrl: String?) {
        _state.value = _state.value.copy(
            profileImg = _state.value.profileImg.copy(imagePath = imgUrl ?: ""),
            imgSelectedState = ImgModifyState.ImgSelected
        )
    }
}