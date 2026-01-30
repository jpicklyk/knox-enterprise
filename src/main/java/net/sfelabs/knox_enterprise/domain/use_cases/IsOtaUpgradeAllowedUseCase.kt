package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsOtaUpgradeAllowedUseCase: WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val restrictionPolicy =
        EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(restrictionPolicy.isOTAUpgradeAllowed)
    }
}