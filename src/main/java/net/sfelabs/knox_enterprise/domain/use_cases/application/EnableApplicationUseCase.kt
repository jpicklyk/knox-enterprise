package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Enable an application on the device.
 * @param params Package name of the application to enable
 */
class EnableApplicationUseCase : WithAndroidApplicationContext, SuspendingUseCase<String, Boolean>() {
    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return ApiResult.Success(data = applicationPolicy.setEnableApplication(params))
    }
}
