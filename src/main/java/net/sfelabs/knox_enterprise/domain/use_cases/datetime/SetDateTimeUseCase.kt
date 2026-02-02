package net.sfelabs.knox_enterprise.domain.use_cases.datetime

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Set the device date and time.
 */
class SetDateTimeUseCase : WithAndroidApplicationContext, SuspendingUseCase<SetDateTimeUseCase.Params, Boolean>() {
    data class Params(
        val year: Int,
        val month: Int,
        val day: Int,
        val hour: Int,
        val minute: Int,
        val second: Int
    )

    private val dateTimePolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).dateTimePolicy
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        val result = dateTimePolicy.setDateTime(
            params.year,
            params.month,
            params.day,
            params.hour,
            params.minute,
            params.second
        )
        return ApiResult.Success(data = result)
    }
}
