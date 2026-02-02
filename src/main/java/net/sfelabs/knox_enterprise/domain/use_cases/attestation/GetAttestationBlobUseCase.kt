package net.sfelabs.knox_enterprise.domain.use_cases.attestation

import com.samsung.android.knox.EnterpriseKnoxManager
import com.samsung.android.knox.integrity.EnhancedAttestationPolicyCallback
import com.samsung.android.knox.integrity.EnhancedAttestationResult
import com.samsung.android.knox.integrity.EnhancedAttestationResult.ERROR_NONE
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Use case to wrap the asynchronous callback into sequential code since we are not attempting to
 * make an external call to a server.
 */
class GetAttestationBlobUseCase: WithAndroidApplicationContext, SuspendingUseCase<String, ByteArray>() {
    private val attestationPolicy =
        EnterpriseKnoxManager.getInstance(applicationContext).enhancedAttestationPolicy

    override suspend fun execute(params: String): ApiResult<ByteArray> {
        return suspendCancellableCoroutine { continuation ->
            attestationPolicy.startAttestation(params, object: EnhancedAttestationPolicyCallback() {
                override fun onAttestationFinished(result: EnhancedAttestationResult) {
                    if (!continuation.isActive) return

                    if (result.error == ERROR_NONE) {
                        continuation.resume(ApiResult.Success(result.blob))
                    } else {
                        continuation.resume(
                            ApiResult.Error(
                                DefaultApiError.UnexpectedError(
                                    "Attestation error (${result.error}) was encountered with reason: ${result.reason}"
                                )
                            )
                        )
                    }
                }
            })
        }
    }
}
