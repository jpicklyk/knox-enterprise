package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowUsbHostStorageUseCase: WithAndroidApplicationContext, SuspendingUseCase<Boolean, Unit>() {
    private val restrictionPolicy =
        EnterpriseDeviceManager.getInstance(applicationContext)
        .restrictionPolicy

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        val result = restrictionPolicy.allowUsbHostStorage(params)
        return if (result)
            ApiResult.Success(Unit)
        else
            ApiResult.Error(
                DefaultApiError.UnexpectedError(
                    "The API allowUsbHostStorage($params) failed"
                )
            )
    }
}