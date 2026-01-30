package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetCCModeUseCase: WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val restrictionPolicy =
        EnterpriseKnoxManager.getInstance(applicationContext).advancedRestrictionPolicy

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (restrictionPolicy.setCCMode(params)) {
            true -> {
                ApiResult.Success(data = params)
            }

            false -> {
                ApiResult.Error(
                    DefaultApiError.UnexpectedError(
                        "Failure occurred applying setCCMode($params)"
                    )
                )
            }
        }
    }
}