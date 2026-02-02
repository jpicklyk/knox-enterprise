package net.sfelabs.knox_enterprise.domain.use_cases.system

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetPowerMenuLockedStateUseCase : SuspendingUseCase<Boolean, Unit>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        return when (val result = systemManager.setPowerMenuLockedState(params)) {
            CustomDeviceManager.SUCCESS -> ApiResult.Success(Unit)
            else -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to set power menu locked state: error code $result"))
        }
    }
}
