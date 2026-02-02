package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowNfcStateChangeUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val nfcPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).nfcPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (nfcPolicy.allowNFCStateChange(params)) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set NFC state change allowance to $params")
            )
        }
    }
}
