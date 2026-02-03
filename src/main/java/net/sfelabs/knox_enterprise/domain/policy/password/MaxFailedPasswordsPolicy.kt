package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMaxFailedPasswordsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMaxFailedPasswordsUseCase

/**
 * Policy to enforce maximum failed password attempts before device wipe.
 *
 * STIG V-268928 requires limiting failed unlock attempts to prevent brute force attacks.
 * When enabled, sets the maximum to [STIG_MAX_ATTEMPTS] (10).
 * When disabled, removes the restriction (sets to 0).
 *
 * After the maximum attempts are exceeded, the device will be wiped.
 */
@PolicyDefinition(
    title = "Max Failed Password Attempts",
    description = "Limit failed unlock attempts per STIG V-268928. When enabled, device wipes after 10 failed attempts.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MaxFailedPasswordsPolicy : BooleanStatePolicy() {

    private val getUseCase = GetMaxFailedPasswordsUseCase()
    private val setUseCase = SetMaxFailedPasswordsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> {
                // Enabled if limit is set (> 0) and meets STIG requirement (<= 10)
                val meetsRequirement = result.data in 1..STIG_MAX_ATTEMPTS
                ApiResult.Success(meetsRequirement)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        val value = if (enabled) STIG_MAX_ATTEMPTS else DISABLED_VALUE
        return when (val result = setUseCase(value)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    companion object {
        /** STIG V-268928 requires max 10 failed attempts */
        const val STIG_MAX_ATTEMPTS = 10
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
