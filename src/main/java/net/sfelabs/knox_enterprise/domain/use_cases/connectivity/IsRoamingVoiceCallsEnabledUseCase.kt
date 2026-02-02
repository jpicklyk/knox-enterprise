package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsRoamingVoiceCallsEnabledUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val roamingPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).roamingPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = roamingPolicy.isRoamingVoiceCallsEnabled)
    }
}
