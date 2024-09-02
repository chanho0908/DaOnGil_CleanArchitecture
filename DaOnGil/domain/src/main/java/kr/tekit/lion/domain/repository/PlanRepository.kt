package kr.tekit.lion.domain.repository

import kr.tekit.lion.domain.model.schedule.MyElapsedSchedules
import kr.tekit.lion.domain.model.schedule.MyUpcomingSchedules
import kr.tekit.lion.domain.exception.Result

interface PlanRepository {
    suspend fun getMyUpcomingScheduleList(page: Int): Result<MyUpcomingSchedules>

    suspend fun getMyElapsedScheduleList(page: Int): Result<MyElapsedSchedules>

}