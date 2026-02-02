package net.sfelabs.knox_enterprise.domain.use_cases.datetime

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Set the device time format.
 * @param params Time format string (e.g., "12" for 12-hour, "24" for 24-hour)
 */
class SetTimeFormatUseCase : WithAndroidApplicationContext, SuspendingUseCase<String, Boolean>() {
    private val dateTimePolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).dateTimePolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return ApiResult.Success(data = dateTimePolicy.setTimeFormat(params))
    }
}
