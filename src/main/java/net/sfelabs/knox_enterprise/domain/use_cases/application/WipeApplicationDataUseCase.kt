package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Wipe application data for a specific application.
 * @param params Package name of the application
 */
class WipeApplicationDataUseCase : WithAndroidApplicationContext, SuspendingUseCase<String, Boolean>() {
    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return ApiResult.Success(data = applicationPolicy.wipeApplicationData(params))
    }
}
