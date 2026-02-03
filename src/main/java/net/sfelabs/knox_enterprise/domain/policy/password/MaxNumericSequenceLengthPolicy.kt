package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.ConfigurableStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyParameters
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMaxNumericSequenceLengthUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMaxNumericSequenceLengthUseCase

/**
 * Policy to enforce maximum numeric sequence length in passwords.
 *
 * STIG V-268925 requires limiting sequential numbers in passwords.
 * When enabled, enforces the configured maximum sequence length.
 * When disabled, removes the restriction (sets to 0).
 *
 * This prevents passwords with sequences like "1234" or "9876".
 */
@PolicyDefinition(
    title = "Password Max Numeric Sequence",
    description = "Limit sequential numbers in passwords per STIG V-268925. Configure the max allowed sequential digits.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MaxNumericSequenceLengthPolicy : ConfigurableStatePolicy<
    MaxSequenceLengthState,
    Int,
    MaxSequenceLengthConfiguration
>(stateMapping = StateMapping.DIRECT) {

    private val getUseCase = GetMaxNumericSequenceLengthUseCase()
    private val setUseCase = SetMaxNumericSequenceLengthUseCase()

    override val configuration = MaxSequenceLengthConfiguration(
        stateMapping = stateMapping,
        optionLabel = "Max Numeric Sequence"
    )

    override val defaultValue = MaxSequenceLengthState(
        isEnabled = false,
        maxSequence = MaxSequenceLengthState.DEFAULT_MAX_SEQUENCE
    )

    override suspend fun getState(parameters: PolicyParameters): MaxSequenceLengthState {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> configuration.fromApiData(result.data)
            is ApiResult.Error -> defaultValue.copy(
                error = result.apiError,
                exception = result.exception
            )
            ApiResult.NotSupported -> defaultValue.copy(isSupported = false)
        }
    }

    override suspend fun setState(state: MaxSequenceLengthState): ApiResult<Unit> {
        return when (val result = setUseCase(configuration.toApiData(state))) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
