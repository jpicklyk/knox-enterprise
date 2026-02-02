package net.sfelabs.knox_enterprise.domain.use_cases.browser

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Set browser autofill setting (enable or disable form autofill).
 */
class SetAutoFillSettingUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val browserPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).browserPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return ApiResult.Success(data = browserPolicy.setAutoFillSetting(params))
    }
}
