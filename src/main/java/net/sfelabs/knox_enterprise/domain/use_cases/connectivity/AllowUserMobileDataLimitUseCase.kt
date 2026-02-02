package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowUserMobileDataLimitUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val restrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (restrictionPolicy.allowUserMobileDataLimit(params)) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set user mobile data limit allowance to $params")
            )
        }
    }
}
