package net.sfelabs.knox_enterprise.domain.use_cases.display

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

class SetAutoRotationStateUseCase : SuspendingUseCase<SetAutoRotationStateUseCase.Params, Unit>() {
    data class Params(val enabled: Boolean, val rotationMode: Int = 0)

    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return systemManager.setAutoRotationState(params.enabled, params.rotationMode)
            .toKnoxApiResult("setAutoRotationState")
    }
}
