package kr.techit.lion.data.repository

import kotlinx.coroutines.flow.Flow
import kr.techit.lion.data.datasource.ActivationDataSource
import kr.techit.lion.domain.model.Activation
import kr.techit.lion.domain.repository.ActivationRepository
import javax.inject.Inject

internal class ActivationRepositoryImpl @Inject constructor(
    private val activationDataSource: ActivationDataSource,
): ActivationRepository {

    override val activation: Flow<Activation>
        get() = activationDataSource.activation

    override suspend fun saveUserActivation(active: Activation) {
        activationDataSource.saveUserActivation(active)
    }
}