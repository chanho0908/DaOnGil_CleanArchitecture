package kr.techit.lion.presentation.myinfo.model

import kr.techit.lion.domain.model.IceInfo
import kr.techit.lion.domain.model.PersonalInfo
import kr.techit.lion.presentation.delegate.NetworkState

data class MyInfoState(
    val personalInfo: PersonalInfo,
    val iceInfo: IceInfo,
    val profileImg: UserProfileImg,
    val personalModifyNetworkState: NetworkState,
    val iceModifyNetworkState: NetworkState,
    val imgSelectedState: ImgModifyState,
    val isPersonalInfoModified: Boolean
) {
    companion object {
        fun create(): MyInfoState {
            return MyInfoState(
                personalInfo = PersonalInfo.create(),
                iceInfo = IceInfo.create(),
                profileImg = UserProfileImg(""),
                personalModifyNetworkState = NetworkState.Loading,
                iceModifyNetworkState = NetworkState.Loading,
                imgSelectedState = ImgModifyState.ImgUnSelected,
                isPersonalInfoModified = false
            )
        }
    }
}
