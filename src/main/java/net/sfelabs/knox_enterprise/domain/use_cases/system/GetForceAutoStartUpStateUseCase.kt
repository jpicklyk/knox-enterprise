package net.sfelabs.knox_enterprise.domain.use_cases.system

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get force auto startup state.
 * @return Time in minutes for auto startup (0 if disabled)
 */
class GetForceAutoStartUpStateUseCase : SuspendingUseCase<Unit, Int>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(data = systemManager.forceAutoStartUpState)
    }
}
