package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.ConfigurableStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyParameters
import net.sfelabs.knox.core.feature.api.StateMapping
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
 * When enabled, the selected biometric authentication type is allowed.
 * When disabled, only password/PIN unlock is permitted.
 */
@PolicyDefinition(
    title = "Biometric Authentication",
    description = "Control biometric unlock (fingerprint, face, iris) per STIG V-268936. Configure which biometric types are allowed.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class BiometricAuthenticationPolicy : ConfigurableStatePolicy<
    BiometricAuthenticationState,
    BiometricAuthDto,
    BiometricAuthenticationConfiguration
>(stateMapping = StateMapping.DIRECT) {

    private val isEnabledUseCase = IsBiometricAuthenticationEnabledUseCase()
    private val setEnabledUseCase = SetBiometricAuthenticationEnabledUseCase()

    override val configuration = BiometricAuthenticationConfiguration(stateMapping = stateMapping)

    override val defaultValue = BiometricAuthenticationState(
        isEnabled = false,
        biometricType = BiometricAuthType.ALL
    )

    override suspend fun getState(parameters: PolicyParameters): BiometricAuthenticationState {
        // Check if all biometric types are enabled (for initial state)
        return when (val result = isEnabledUseCase(BiometricAuthType.ALL)) {
            is ApiResult.Success -> BiometricAuthenticationState(
                isEnabled = result.data,
                biometricType = BiometricAuthType.ALL
            )
            is ApiResult.Error -> defaultValue.copy(
                error = result.apiError,
                exception = result.exception
            )
            ApiResult.NotSupported -> defaultValue.copy(isSupported = false)
        }
    }

    override suspend fun setState(state: BiometricAuthenticationState): ApiResult<Unit> {
        val apiData = configuration.toApiData(state)
        val params = SetBiometricAuthenticationEnabledUseCase.Params(
            biometricType = apiData.type,
            enabled = apiData.enabled
        )
        return when (val result = setEnabledUseCase(params)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
