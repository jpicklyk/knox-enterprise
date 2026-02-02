package net.sfelabs.knox_enterprise.domain.use_cases.audio

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get system sounds enabled state.
 * @return The enabled state value
 */
class GetSystemSoundsEnabledStateUseCase : SuspendingUseCase<Unit, Int>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(data = systemManager.systemSoundsEnabledState)
    }
}
