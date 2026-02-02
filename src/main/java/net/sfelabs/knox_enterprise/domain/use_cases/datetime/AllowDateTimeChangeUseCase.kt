package net.sfelabs.knox_enterprise.domain.use_cases.datetime

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Allow or disallow date/time changes on the device.
 */
class AllowDateTimeChangeUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Unit>() {
    private val dateTimePolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).dateTimePolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        dateTimePolicy.setDateTimeChangeEnabled(params)
        return ApiResult.Success(Unit)
    }
}
