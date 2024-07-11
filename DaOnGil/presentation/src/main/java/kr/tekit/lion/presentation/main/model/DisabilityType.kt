package kr.tekit.lion.presentation.main.model

sealed class DisabilityType(val code: Long) {
    abstract val filterCodes: Set<Long>
}
data object PhysicalDisability : DisabilityType(1) {
    override val filterCodes: Set<Long>
        get() = setOf(Parking.code, Wheelchair.code, Elevator.code, RestRoom.code, Seat.code)
}

data object VisualImpairment : DisabilityType(2) {
    override val filterCodes: Set<Long>
        get() = setOf(Braileblock.code, HelpDog.code, Guide.code, AudioGuide.code)
}

data object HearingImpairment : DisabilityType(3) {
    override val filterCodes: Set<Long>
        get() = setOf(SignGuide.code, VideoGuide.code)
}

data object InfantFamily : DisabilityType(4) {
    override val filterCodes: Set<Long>
        get() = setOf(Stroller.code, LactationRoom.code, BabySpareChair.code)
}

data object ElderlyPeople : DisabilityType(5) {
    override val filterCodes: Set<Long>
        get() = setOf(WheelchairLent.code)
}
