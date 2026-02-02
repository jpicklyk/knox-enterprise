package net.sfelabs.knox_enterprise.domain.use_cases.datetime

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get the device timezone.
 * @return Timezone ID (e.g., "America/New_York", "Europe/London")
 */
class GetTimeZoneUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, String>() {
    private val dateTimePolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).dateTimePolicy
    }

    override suspend fun execute(params: Unit): ApiResult<String> {
        return ApiResult.Success(data = dateTimePolicy.timeZone)
    }
}
