package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetCCModeUseCase: WithAndroidApplicationContext, SuspendingUseCase<SetCCModeUseCase.Params, Boolean>() {
    class Params(val enable: Boolean)

    private val restrictionPolicy =
        EnterpriseKnoxManager.getInstance(applicationContext).advancedRestrictionPolicy

    suspend operator fun invoke(enable: Boolean): ApiResult<Boolean> {
        return invoke(Params(enable))
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return when (restrictionPolicy.setCCMode(params.enable)) {
            true -> {
                ApiResult.Success(data = params.enable)
            }

            false -> {
                ApiResult.Error(
                    DefaultApiError.UnexpectedError(
                        "Failure occurred applying setCCMode(${params.enable})"
                    )
                )
            }
        }
    }
}