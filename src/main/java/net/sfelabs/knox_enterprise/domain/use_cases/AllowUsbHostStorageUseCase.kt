package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowUsbHostStorageUseCase: WithAndroidApplicationContext, SuspendingUseCase<AllowUsbHostStorageUseCase.Params, Unit>() {
    class Params(val allow: Boolean)

    private val restrictionPolicy =
        EnterpriseDeviceManager.getInstance(applicationContext)
        .restrictionPolicy

    suspend fun invoke(allow: Boolean): ApiResult<Unit> {
        return invoke(Params(allow))
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        val result = restrictionPolicy.allowUsbHostStorage(params.allow)
        return if (result)
            ApiResult.Success(Unit)
        else
            ApiResult.Error(
                DefaultApiError.UnexpectedError(
                    "The API allowUsbHostStorage($params.allow) failed"
                )
            )
    }
}