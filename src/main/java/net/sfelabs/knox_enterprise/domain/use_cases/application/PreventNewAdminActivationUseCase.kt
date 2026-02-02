package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Prevent or allow new admin activation.
 * @param params true to prevent, false to allow
 */
class PreventNewAdminActivationUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return ApiResult.Success(data = applicationPolicy.preventNewAdminActivation(params))
    }
}
