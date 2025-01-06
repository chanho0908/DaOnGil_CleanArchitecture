package kr.techit.lion.presentation.main.myinfo.vm.model

data class ProfileState(
    val myInfo: MyInfoUiModel
){
    companion object{
        fun create(): ProfileState {
            return ProfileState(
                myInfo = MyInfoUiModel()
            )
        }
    }
}