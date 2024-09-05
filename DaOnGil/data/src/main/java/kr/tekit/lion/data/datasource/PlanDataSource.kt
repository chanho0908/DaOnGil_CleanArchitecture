package kr.tekit.lion.data.datasource

import kr.tekit.lion.data.common.execute
import kr.tekit.lion.data.service.PlanService
import kr.tekit.lion.domain.exception.Result
import kr.tekit.lion.domain.model.MyMainSchedule
import kr.tekit.lion.domain.model.schedule.MyElapsedSchedules
import kr.tekit.lion.domain.model.schedule.MyUpcomingSchedules
import javax.inject.Inject

internal class PlanDataSource @Inject constructor(
    private val planService: PlanService
) {
    companion object {
        private const val PAGE_SIZE = 10
    }

    suspend fun getMyUpcomingScheduleList(page: Int): Result<MyUpcomingSchedules> = execute {
        planService.getMyUpcomingScheduleList(PAGE_SIZE, page).toDomainModel()
    }

    suspend fun getMyElapsedScheduleList(page: Int): Result<MyElapsedSchedules> = execute {
        planService.getMyElapsedScheduleList(PAGE_SIZE, page).toDomainModel()
    }

    suspend fun getMyMainSchedule(): Result<List<MyMainSchedule?>?> = execute {
        planService.getMyMainSchedule().toDomainModel()
    }
}