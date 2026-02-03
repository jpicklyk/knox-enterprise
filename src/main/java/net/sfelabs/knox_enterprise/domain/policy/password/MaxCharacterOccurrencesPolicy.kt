package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMaxCharacterOccurrencesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMaxCharacterOccurrencesUseCase

/**
 * Policy to enforce maximum character occurrences in passwords.
 *
 * STIG V-268925 requires limiting repeated characters in passwords.
 * When enabled, sets the maximum to [STIG_MAX_OCCURRENCES] (4).
 * When disabled, removes the restriction (sets to 0).
 *
 * This prevents passwords like "aaaa1234" where a character repeats too many times.
 */
@PolicyDefinition(
    title = "Password Max Character Occurrences",
    description = "Limit repeated characters in passwords per STIG V-268925. When enabled, no character can appear more than 4 times.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MaxCharacterOccurrencesPolicy : BooleanStatePolicy() {

    private val getUseCase = GetMaxCharacterOccurrencesUseCase()
    private val setUseCase = SetMaxCharacterOccurrencesUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> {
                // Enabled if limit is set (> 0) and meets STIG requirement (<= 4)
                val meetsRequirement = result.data in 1..STIG_MAX_OCCURRENCES
                ApiResult.Success(meetsRequirement)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        val value = if (enabled) STIG_MAX_OCCURRENCES else DISABLED_VALUE
        return when (val result = setUseCase(value)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    companion object {
        /** STIG V-268925 requires max 4 occurrences of any character */
        const val STIG_MAX_OCCURRENCES = 4
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
