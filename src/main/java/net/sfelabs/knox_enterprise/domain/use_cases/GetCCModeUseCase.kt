package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class GetCCModeUseCase: WithAndroidApplicationContext, SuspendingUseCase<Unit, Int>() {
    private val restrictionPolicy =
        EnterpriseKnoxManager.getInstance(applicationContext).advancedRestrictionPolicy

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(restrictionPolicy.ccModeState)
    }
}