package net.sfelabs.knox_enterprise.domain.use_cases.system

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Set force auto shutdown state.
 * @param params Time in minutes for auto shutdown (0 to disable)
 */
class SetForceAutoShutDownStateUseCase : SuspendingUseCase<Int, Unit>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Int): ApiResult<Unit> {
        return when (val result = systemManager.setForceAutoShutDownState(params)) {
            CustomDeviceManager.SUCCESS -> ApiResult.Success(Unit)
            else -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to set force auto shutdown state: error code $result"))
        }
    }
}
