package net.sfelabs.knox_enterprise.domain.use_cases.security

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsMultifactorAuthenticationEnabledUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = passwordPolicy.isMultifactorAuthenticationEnabled)
    }
}
