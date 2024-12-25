package kr.techit.lion.domain.repository

import kotlinx.coroutines.flow.Flow
import kr.techit.lion.domain.model.Activation

interface ActivationRepository {
    val activation: Flow<Activation>
    suspend fun saveUserActivation(active: Activation)
}