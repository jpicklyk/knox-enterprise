package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.ConfigurableStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyParameters
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetPasswordLockDelayUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetPasswordLockDelayUseCase

/**
 * Policy to enforce password lock delay (grace period).
 *
 * STIG V-268926 requires limiting the time before password is required after screen off.
 * When enabled, enforces the configured delay in seconds.
 * When disabled, sets a permissive delay (5 minutes).
 *
 * This ensures the device locks quickly after the screen turns off.
 */
@PolicyDefinition(
    title = "Password Lock Delay",
    description = "Set password lock delay per STIG V-268926. Configure the seconds before password is required after screen off.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class PasswordLockDelayPolicy : ConfigurableStatePolicy<
    PasswordLockDelayState,
    Int,
    PasswordLockDelayConfiguration
>(stateMapping = StateMapping.DIRECT) {

    private val getUseCase = GetPasswordLockDelayUseCase()
    private val setUseCase = SetPasswordLockDelayUseCase()

    override val configuration = PasswordLockDelayConfiguration(stateMapping = stateMapping)

    override val defaultValue = PasswordLockDelayState(
        isEnabled = false,
        delaySeconds = PasswordLockDelayState.DEFAULT_DELAY_SECONDS
    )

    override suspend fun getState(parameters: PolicyParameters): PasswordLockDelayState {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> configuration.fromApiData(result.data)
            is ApiResult.Error -> defaultValue.copy(
                error = result.apiError,
                exception = result.exception
            )
            ApiResult.NotSupported -> defaultValue.copy(isSupported = false)
        }
    }

    override suspend fun setState(state: PasswordLockDelayState): ApiResult<Unit> {
        return when (val result = setUseCase(configuration.toApiData(state))) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
