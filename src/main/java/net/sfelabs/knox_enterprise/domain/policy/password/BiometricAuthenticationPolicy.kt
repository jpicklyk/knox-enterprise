package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.model.BiometricAuthType
import net.sfelabs.knox_enterprise.domain.use_cases.password.IsBiometricAuthenticationEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetBiometricAuthenticationEnabledUseCase

/**
 * Policy to control biometric authentication.
 *
 * STIG V-268936 allows biometric authentication to be enabled, but it must be
 * configured alongside strong password requirements.
 *
 * This policy controls whether biometric unlock (fingerprint, face, iris) is permitted.
 * When enabled, biometric authentication is allowed.
 * When disabled, only password/PIN unlock is permitted.
 */
@PolicyDefinition(
    title = "Biometric Authentication",
    description = "Control biometric unlock (fingerprint, face, iris) per STIG V-268936.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class BiometricAuthenticationPolicy : BooleanStatePolicy() {

    private val isEnabledUseCase = IsBiometricAuthenticationEnabledUseCase()
    private val setEnabledUseCase = SetBiometricAuthenticationEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        // Check if all biometric types are enabled
        return when (val result = isEnabledUseCase(BiometricAuthType.ALL)) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        val params = SetBiometricAuthenticationEnabledUseCase.Params(
            biometricType = BiometricAuthType.ALL,
            enabled = enabled
        )
        return when (val result = setEnabledUseCase(params)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
