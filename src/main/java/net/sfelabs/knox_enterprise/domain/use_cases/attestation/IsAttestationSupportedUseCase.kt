package net.sfelabs.knox_enterprise.domain.use_cases.attestation

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsAttestationSupportedUseCase: WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val attestationPolicy =
        EnterpriseKnoxManager.getInstance(applicationContext).enhancedAttestationPolicy

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(attestationPolicy.isSupported)
    }
}