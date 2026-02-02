package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowWifiScanningUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val advancedRestrictionPolicy by lazy {
        EnterpriseKnoxManager.getInstance(applicationContext).advancedRestrictionPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (advancedRestrictionPolicy.allowWifiScanning(params)) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set WiFi scanning allowance to $params")
            )
        }
    }
}
