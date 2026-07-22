package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

class SetMobileDataRoamingStateUseCase: SuspendingUseCase<Boolean, Unit>() {
    private val settingsManager by lazy { CustomDeviceManager.getInstance().settingsManager }

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        return settingsManager.setMobileDataRoamingState(params).toKnoxApiResult("setMobileDataRoamingState")
    }
}