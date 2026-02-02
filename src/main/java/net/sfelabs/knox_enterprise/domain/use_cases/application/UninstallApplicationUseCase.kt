package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Uninstall an application from the device.
 */
class UninstallApplicationUseCase : WithAndroidApplicationContext, SuspendingUseCase<UninstallApplicationUseCase.Params, Boolean>() {
    data class Params(val packageName: String, val keepData: Boolean = false)

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return ApiResult.Success(data = applicationPolicy.uninstallApplication(params.packageName, params.keepData))
    }
}
