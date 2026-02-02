package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Check if new admin installation is enabled.
 * @param params true to check current state
 */
class IsNewAdminInstallationEnabledUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return ApiResult.Success(data = applicationPolicy.isNewAdminInstallationEnabled(params))
    }
}
