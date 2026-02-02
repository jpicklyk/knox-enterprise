package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class ActivateWifiSsidRestrictionUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val wifiPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).wifiPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (wifiPolicy.activateWifiSsidRestriction(params)) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set WiFi SSID restriction active to $params")
            )
        }
    }
}
