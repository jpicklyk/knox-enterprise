package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetNfcEnabledUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val nfcPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).nfcPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        // NFC can only be started, there's no stopNFC API - just toggle to the desired state
        val result = nfcPolicy.startNFC(params)
        return when (result) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set NFC enabled to $params")
            )
        }
    }
}
