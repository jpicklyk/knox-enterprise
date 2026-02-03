package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMaxCharacterSequenceLengthUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMaxCharacterSequenceLengthUseCase

/**
 * Policy to enforce maximum alphabetic character sequence length in passwords.
 *
 * STIG V-268925 requires limiting sequential characters in passwords.
 * When enabled, sets the maximum to [STIG_MAX_SEQUENCE] (3).
 * When disabled, removes the restriction (sets to 0).
 *
 * This prevents passwords with sequences like "abcd" or "zyxw".
 */
@PolicyDefinition(
    title = "Password Max Character Sequence",
    description = "Limit sequential characters in passwords per STIG V-268925. When enabled, max 3 sequential characters allowed.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MaxCharacterSequenceLengthPolicy : BooleanStatePolicy() {

    private val getUseCase = GetMaxCharacterSequenceLengthUseCase()
    private val setUseCase = SetMaxCharacterSequenceLengthUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> {
                // Enabled if limit is set (> 0) and meets STIG requirement (<= 3)
                val meetsRequirement = result.data in 1..STIG_MAX_SEQUENCE
                ApiResult.Success(meetsRequirement)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        val value = if (enabled) STIG_MAX_SEQUENCE else DISABLED_VALUE
        return when (val result = setUseCase(value)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    companion object {
        /** STIG V-268925 requires max 3 sequential characters */
        const val STIG_MAX_SEQUENCE = 3
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
