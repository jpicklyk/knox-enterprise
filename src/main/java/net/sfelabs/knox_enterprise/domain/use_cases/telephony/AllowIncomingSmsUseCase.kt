package net.sfelabs.knox_enterprise.domain.use_cases.telephony

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowIncomingSmsUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val phoneRestrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).phoneRestrictionPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (phoneRestrictionPolicy.allowIncomingSms(params)) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to set incoming SMS allowance to $params"))
        }
    }
}
