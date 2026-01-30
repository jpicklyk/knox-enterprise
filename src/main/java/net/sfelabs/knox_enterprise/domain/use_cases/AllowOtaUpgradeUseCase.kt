package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowOtaUpgradeUseCase: WithAndroidApplicationContext, SuspendingUseCase<AllowOtaUpgradeUseCase.Params, Boolean>() {
    data class Params(val enable: Boolean)

    private val restrictionPolicy = EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy

    suspend operator fun invoke(enable: Boolean): ApiResult<Boolean> {
        return invoke(Params(enable))
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return when (restrictionPolicy.allowOTAUpgrade(params.enable)) {
            true -> ApiResult.Success(data = params.enable)

            false -> ApiResult.Error(DefaultApiError
                .UnexpectedError("Failure occurred applying API allowOTAUpgrade(${params.enable})"))
        }
    }
}