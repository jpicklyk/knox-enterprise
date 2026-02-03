package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMaxNumericSequenceLengthUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMaxNumericSequenceLengthUseCase

/**
 * Policy to enforce maximum numeric sequence length in passwords.
 *
 * STIG V-268925 requires limiting sequential numbers in passwords.
 * When enabled, sets the maximum to [STIG_MAX_SEQUENCE] (3).
 * When disabled, removes the restriction (sets to 0).
 *
 * This prevents passwords with sequences like "1234" or "9876".
 */
@PolicyDefinition(
    title = "Password Max Numeric Sequence",
    description = "Limit sequential numbers in passwords per STIG V-268925. When enabled, max 3 sequential digits allowed.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MaxNumericSequenceLengthPolicy : BooleanStatePolicy() {

    private val getUseCase = GetMaxNumericSequenceLengthUseCase()
    private val setUseCase = SetMaxNumericSequenceLengthUseCase()

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
        /** STIG V-268925 requires max 3 sequential digits */
        const val STIG_MAX_SEQUENCE = 3
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
