package net.sfelabs.knox_enterprise.domain.use_cases.datetime

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Set the device timezone.
 * @param params Timezone ID (e.g., "America/New_York", "Europe/London")
 */
class SetTimeZoneUseCase : WithAndroidApplicationContext, SuspendingUseCase<String, Unit>() {
    private val dateTimePolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).dateTimePolicy
    }

    override suspend fun execute(params: String): ApiResult<Unit> {
        dateTimePolicy.setTimeZone(params)
        return ApiResult.Success(Unit)
    }
}
