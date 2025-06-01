package com.fls.dndproject_frontend.data.repository

import com.fls.dndproject_frontend.data.model.equipment.CreateEquipmentDto
import com.fls.dndproject_frontend.data.source.remote.EquipmentServiceClient
import com.fls.dndproject_frontend.domain.model.Equipment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class EquipmentRestRepository(private val equipmentServiceClient: EquipmentServiceClient) {

    fun getAllEquipment(): Flow<List<Equipment>> =
        observeQuery(retryTime = 2000) {
            equipmentServiceClient
                .getAllEquipment()
                .map { it.toEquipment() }
        }

    fun <T> observeQuery(retryTime: Long = 5000, query: suspend () -> List<T>): Flow<List<T>> = flow {
        var lastResult: List<T> = emptyList()
        var isFirstEmission = true

        while (true) {
            try {
                val newResult = query()

                if (isFirstEmission || newResult != lastResult) {
                    lastResult = newResult
                    emit(newResult)
                    isFirstEmission = false
                }
            } catch (e: Exception) {
            }
            delay(retryTime)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun save(equipment: Equipment) {
        equipmentServiceClient.createEquipment(CreateEquipmentDto.fromEquipment(equipment))
    }

    suspend fun delete(equipmentId: Int) {
        equipmentServiceClient.deleteEquipment(equipmentId)
    }
}
