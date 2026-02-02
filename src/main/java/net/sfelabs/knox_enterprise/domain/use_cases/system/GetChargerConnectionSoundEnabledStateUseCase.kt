package net.sfelabs.knox_enterprise.domain.use_cases.system

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class GetChargerConnectionSoundEnabledStateUseCase : SuspendingUseCase<Unit, Boolean>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = systemManager.chargerConnectionSoundEnabledState)
    }
}
