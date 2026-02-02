package net.sfelabs.knox_enterprise.domain.use_cases.connectivity

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsBleAllowedUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val advancedRestrictionPolicy by lazy {
        EnterpriseKnoxManager.getInstance(applicationContext).advancedRestrictionPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = advancedRestrictionPolicy.isBLEAllowed)
    }
}
