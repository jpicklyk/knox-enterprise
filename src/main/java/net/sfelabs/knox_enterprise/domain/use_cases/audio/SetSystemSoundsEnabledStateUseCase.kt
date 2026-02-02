package net.sfelabs.knox_enterprise.domain.use_cases.audio

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Set system sounds enabled state.
 */
class SetSystemSoundsEnabledStateUseCase : SuspendingUseCase<SetSystemSoundsEnabledStateUseCase.Params, Unit>() {
    data class Params(val type: Int, val enabled: Int)

    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return when (val result = systemManager.setSystemSoundsEnabledState(params.type, params.enabled)) {
            CustomDeviceManager.SUCCESS -> ApiResult.Success(Unit)
            else -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to set system sounds enabled state: error code $result"))
        }
    }
}
