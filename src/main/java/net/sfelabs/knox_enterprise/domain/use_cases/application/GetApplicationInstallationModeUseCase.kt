package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get the application installation mode.
 * @return APPLICATION_INSTALLATION_MODE_ALLOW or APPLICATION_INSTALLATION_MODE_DISALLOW
 */
class GetApplicationInstallationModeUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Int>() {
    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(data = applicationPolicy.applicationInstallationMode)
    }
}
