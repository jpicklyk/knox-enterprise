package net.sfelabs.knox_enterprise.domain.use_cases.attestation

import com.samsung.knox.attesation.blobvalidator.library.AttestationResult
import com.samsung.knox.attesation.blobvalidator.library.EABlobVerifier
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Attestation verifier from HQ integrated into a simple API use case call.
 *
 * Currently the attestation result verdict will report No when HDM CP is disabled.
 *
 * NOTE: THIS IS NOT A SECURE FORM OF VALIDATING THE ATTESTATION BLOB.  AN EXTERNAL SERVER MUST BE
 * USED FOR ANY FORM OF TRUST!
 */
class ValidateAttestationUseCase {
    operator fun invoke(nonce: String, blob: ByteArray): ApiResult<AttestationResult> {
        return try {
            ApiResult.Success(EABlobVerifier.verify(nonce, blob))
        } catch (e: Exception) {
            ApiResult.Error(DefaultApiError.UnexpectedError(e.message!!))
        }
    }
}