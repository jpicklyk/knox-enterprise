package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.ConfigurableStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyParameters
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMaxCharacterOccurrencesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMaxCharacterOccurrencesUseCase

/**
 * Policy to enforce maximum character occurrences in passwords.
 *
 * STIG V-268925 requires limiting repeated characters in passwords.
 * When enabled, enforces the configured maximum occurrences.
 * When disabled, removes the restriction (sets to 0).
 *
 * This prevents passwords like "aaaa1234" where a character repeats too many times.
 */
@PolicyDefinition(
    title = "Password Max Character Occurrences",
    description = "Limit repeated characters in passwords per STIG V-268925. Configure max times any character can appear.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MaxCharacterOccurrencesPolicy : ConfigurableStatePolicy<
    MaxCharacterOccurrencesState,
    Int,
    MaxCharacterOccurrencesConfiguration
>(stateMapping = StateMapping.DIRECT) {

    private val getUseCase = GetMaxCharacterOccurrencesUseCase()
    private val setUseCase = SetMaxCharacterOccurrencesUseCase()

    override val configuration = MaxCharacterOccurrencesConfiguration(stateMapping = stateMapping)

    override val defaultValue = MaxCharacterOccurrencesState(
        isEnabled = false,
        maxOccurrences = MaxCharacterOccurrencesState.DEFAULT_MAX_OCCURRENCES
    )

    override suspend fun getState(parameters: PolicyParameters): MaxCharacterOccurrencesState {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> configuration.fromApiData(result.data)
            is ApiResult.Error -> defaultValue.copy(
                error = result.apiError,
                exception = result.exception
            )
            ApiResult.NotSupported -> defaultValue.copy(isSupported = false)
        }
    }

    override suspend fun setState(state: MaxCharacterOccurrencesState): ApiResult<Unit> {
        return when (val result = setUseCase(configuration.toApiData(state))) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
