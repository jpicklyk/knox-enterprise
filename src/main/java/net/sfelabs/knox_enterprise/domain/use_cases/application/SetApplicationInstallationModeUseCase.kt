package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Set the application installation mode.
 * @param params APPLICATION_INSTALLATION_MODE_ALLOW or APPLICATION_INSTALLATION_MODE_DISALLOW
 */
class SetApplicationInstallationModeUseCase : WithAndroidApplicationContext, SuspendingUseCase<Int, Boolean>() {
    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Int): ApiResult<Boolean> {
        return ApiResult.Success(data = applicationPolicy.setApplicationInstallationMode(params))
    }
}
