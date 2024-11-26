package kr.techit.lion.presentation.myinfo.intent

import kr.techit.lion.domain.model.IceInfo
import kr.techit.lion.domain.model.PersonalInfo

sealed interface MyInfoIntent {
    object OnUiEventInitializeUiData : MyInfoIntent
    data class OnUiEventModifyPersonalInfo(val PersonalInfo: PersonalInfo) : MyInfoIntent
    data class OnUiEventModifyIceInfo(val newIceInfo: IceInfo) : MyInfoIntent
    data class OnUiEventSelectProfileImage(val imgUrl: String?) : MyInfoIntent
}

