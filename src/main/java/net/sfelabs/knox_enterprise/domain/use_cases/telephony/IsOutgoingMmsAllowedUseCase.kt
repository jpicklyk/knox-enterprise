package net.sfelabs.knox_enterprise.domain.use_cases.telephony

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsOutgoingMmsAllowedUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val phoneRestrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).phoneRestrictionPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = phoneRestrictionPolicy.isOutgoingMmsAllowed())
    }
}
