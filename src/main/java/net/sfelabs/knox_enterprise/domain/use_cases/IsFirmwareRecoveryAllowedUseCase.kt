package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsFirmwareRecoveryAllowedUseCase: WithAndroidApplicationContext, SuspendingUseCase<IsFirmwareRecoveryAllowedUseCase.Params, Boolean>() {
    class Params(val showMsg: Boolean)
    private val restrictionPolicy = EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy

    suspend operator fun invoke(showMsg: Boolean = true): ApiResult<Boolean> {
        return invoke(Params(showMsg))
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return ApiResult.Success(
            restrictionPolicy.isFirmwareRecoveryAllowed(
                params.showMsg
            )
        )
    }
}