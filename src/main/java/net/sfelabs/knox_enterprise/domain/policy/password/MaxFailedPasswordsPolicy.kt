package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.ConfigurableStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyParameters
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMaxFailedPasswordsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMaxFailedPasswordsUseCase

/**
 * Policy to enforce maximum failed password attempts before device wipe.
 *
 * STIG V-268928 requires limiting failed unlock attempts to prevent brute force attacks.
 * When enabled, enforces the configured maximum attempts.
 * When disabled, removes the restriction (sets to 0).
 *
 * After the maximum attempts are exceeded, the device will be wiped.
 */
@PolicyDefinition(
    title = "Max Failed Password Attempts",
    description = "Limit failed unlock attempts per STIG V-268928. Configure the max attempts before device wipe.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MaxFailedPasswordsPolicy : ConfigurableStatePolicy<
    MaxFailedPasswordsState,
    Int,
    MaxFailedPasswordsConfiguration
>(stateMapping = StateMapping.DIRECT) {

    private val getUseCase = GetMaxFailedPasswordsUseCase()
    private val setUseCase = SetMaxFailedPasswordsUseCase()

    override val configuration = MaxFailedPasswordsConfiguration(stateMapping = stateMapping)

    override val defaultValue = MaxFailedPasswordsState(
        isEnabled = false,
        maxAttempts = MaxFailedPasswordsState.DEFAULT_MAX_ATTEMPTS
    )

    override suspend fun getState(parameters: PolicyParameters): MaxFailedPasswordsState {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> configuration.fromApiData(result.data)
            is ApiResult.Error -> defaultValue.copy(
                error = result.apiError,
                exception = result.exception
            )
            ApiResult.NotSupported -> defaultValue.copy(isSupported = false)
        }
    }

    override suspend fun setState(state: MaxFailedPasswordsState): ApiResult<Unit> {
        return when (val result = setUseCase(configuration.toApiData(state))) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
