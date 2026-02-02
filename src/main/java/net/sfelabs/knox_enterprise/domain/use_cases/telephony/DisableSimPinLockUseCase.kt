package net.sfelabs.knox_enterprise.domain.use_cases.telephony

import com.samsung.android.knox.EnterpriseDeviceManager
import com.samsung.android.knox.restriction.PhoneRestrictionPolicy
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class DisableSimPinLockUseCase : WithAndroidApplicationContext, SuspendingUseCase<String, Boolean>() {
    private val phoneRestrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).phoneRestrictionPolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return when (phoneRestrictionPolicy.disableSimPinLock(params)) {
            PhoneRestrictionPolicy.ERROR_NONE -> ApiResult.Success(data = true)
            PhoneRestrictionPolicy.ERROR_INVALID_INPUT -> ApiResult.Error(DefaultApiError.UnexpectedError("Invalid PIN code"))
            PhoneRestrictionPolicy.ERROR_NOT_SUPPORTED -> ApiResult.Error(DefaultApiError.UnexpectedError("SIM PIN lock not supported"))
            else -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to disable SIM PIN lock"))
        }
    }
}
