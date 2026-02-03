package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.model.BiometricAuthType
import net.sfelabs.knox_enterprise.domain.use_cases.password.IsBiometricAuthenticationEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetBiometricAuthenticationEnabledUseCase

/**
 * Policy to control biometric authentication (fingerprint, face, iris).
 *
 * STIG V-268936 requires disabling face recognition and biometric access
 * to ensure stronger authentication methods are used.
 *
 * This policy controls ALL biometric types. For fine-grained control,
 * use the use cases directly with specific BiometricAuthType values.
 */
@PolicyDefinition(
    title = "Biometric Authentication Enabled",
    description = "Enable or disable biometric authentication methods (fingerprint, face, iris recognition).",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class BiometricAuthenticationEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsBiometricAuthenticationEnabledUseCase()
    private val setUseCase = SetBiometricAuthenticationEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase(BiometricAuthType.ALL)

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> =
        setUseCase(SetBiometricAuthenticationEnabledUseCase.Params(BiometricAuthType.ALL, enabled)).map { }
}
