package net.sfelabs.knox_enterprise.domain.use_cases.attestation

import com.samsung.android.knox.EnterpriseKnoxManager
import com.samsung.android.knox.integrity.EnhancedAttestationPolicyCallback
import com.samsung.android.knox.integrity.EnhancedAttestationResult
import com.samsung.android.knox.integrity.EnhancedAttestationResult.ERROR_NONE
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError
import java.util.UUID
import kotlin.coroutines.suspendCoroutine

/**
 * Use case to wrap the asynchronous callback into sequential code since we are not attempting to
 * make an external call to a server.
 */
class GetAttestationBlobUseCase: WithAndroidApplicationContext, SuspendingUseCase<String, ByteArray>() {
    private val attestationPolicy =
        EnterpriseKnoxManager.getInstance(applicationContext).enhancedAttestationPolicy

    override suspend fun execute(params: String): ApiResult<ByteArray> {
        return suspendCoroutine {
            attestationPolicy.startAttestation(params, object: EnhancedAttestationPolicyCallback() {
                override fun onAttestationFinished(result: EnhancedAttestationResult) {
                    if(result.error == ERROR_NONE) {
                        it.resumeWith(Result.success(ApiResult.Success(result.blob)))
                    } else {
                        it.resumeWith(Result.success(
                            ApiResult.Error(
                                DefaultApiError.UnexpectedError(
                                    "Attestation error (${result.error}) was encountered with reason: ${result.reason}"
                                )
                            )))
                    }
                }

            })
        }
    }
}
