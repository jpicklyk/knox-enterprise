package net.sfelabs.knox_enterprise.domain.use_cases.telephony

import com.samsung.android.knox.EnterpriseDeviceManager
import com.samsung.android.knox.restriction.PhoneRestrictionPolicy
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class ChangeSimPinCodeUseCase : WithAndroidApplicationContext, SuspendingUseCase<ChangeSimPinCodeUseCase.Params, Boolean>() {
    data class Params(val currentPin: String, val newPin: String)

    private val phoneRestrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).phoneRestrictionPolicy
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return when (phoneRestrictionPolicy.changeSimPinCode(params.currentPin, params.newPin)) {
            PhoneRestrictionPolicy.ERROR_NONE -> ApiResult.Success(data = true)
            PhoneRestrictionPolicy.ERROR_INVALID_INPUT -> ApiResult.Error(DefaultApiError.UnexpectedError("Invalid PIN code"))
            PhoneRestrictionPolicy.ERROR_NOT_SUPPORTED -> ApiResult.Error(DefaultApiError.UnexpectedError("SIM PIN change not supported"))
            else -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to change SIM PIN code"))
        }
    }
}
