package net.sfelabs.knox_enterprise.domain.use_cases.security

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetScreenLockPatternVisibilityUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (passwordPolicy.setScreenLockPatternVisibilityEnabled(params)) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set screen lock pattern visibility enabled to $params")
            )
        }
    }
}
