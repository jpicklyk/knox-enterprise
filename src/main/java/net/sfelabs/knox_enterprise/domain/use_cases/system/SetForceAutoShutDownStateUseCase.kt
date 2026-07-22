package net.sfelabs.knox_enterprise.domain.use_cases.system

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

/**
 * Set force auto shutdown state.
 * @param params Time in minutes for auto shutdown (0 to disable)
 */
class SetForceAutoShutDownStateUseCase : SuspendingUseCase<Int, Unit>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Int): ApiResult<Unit> {
        return systemManager.setForceAutoShutDownState(params)
            .toKnoxApiResult("setForceAutoShutDownState")
    }
}
