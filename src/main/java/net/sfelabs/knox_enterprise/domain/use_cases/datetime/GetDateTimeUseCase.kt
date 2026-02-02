package net.sfelabs.knox_enterprise.domain.use_cases.datetime

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import java.util.Date

/**
 * Get the device date and time.
 */
class GetDateTimeUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Date>() {
    private val dateTimePolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).dateTimePolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Date> {
        return ApiResult.Success(data = dateTimePolicy.dateTime)
    }
}
