package net.sfelabs.knox_enterprise.domain.use_cases.datetime

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get the device date format.
 * @return Date format string
 */
class GetDateFormatUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, String>() {
    private val dateTimePolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).dateTimePolicy
    }

    override suspend fun execute(params: Unit): ApiResult<String> {
        return ApiResult.Success(data = dateTimePolicy.dateFormat)
    }
}
