package net.sfelabs.knox_enterprise.domain.use_cases.browser

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get browser autofill setting (whether form autofill is enabled).
 */
class GetAutoFillSettingUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val browserPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).browserPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = browserPolicy.autoFillSetting)
    }
}
