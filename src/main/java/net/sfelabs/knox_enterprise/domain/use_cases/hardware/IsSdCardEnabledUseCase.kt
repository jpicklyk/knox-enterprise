package net.sfelabs.knox_enterprise.domain.use_cases.hardware

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsSdCardEnabledUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val restrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = restrictionPolicy.isSdCardEnabled)
    }
}
