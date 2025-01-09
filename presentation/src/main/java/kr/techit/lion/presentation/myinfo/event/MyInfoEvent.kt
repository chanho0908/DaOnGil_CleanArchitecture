package kr.techit.lion.presentation.myinfo.event

import kr.techit.lion.domain.model.IceInfo
import kr.techit.lion.domain.model.PersonalInfo

sealed interface MyInfoEvent {
    object OnUiEventInitializeUiData : MyInfoEvent
    data class OnUiEventModifyPersonalInfo(val personalInfo: PersonalInfo) : MyInfoEvent
    data class OnUiEventModifyIceInfo(val newIceInfo: IceInfo) : MyInfoEvent
    data class OnUiEventSelectProfileImage(val imgUrl: String?) : MyInfoEvent
}

