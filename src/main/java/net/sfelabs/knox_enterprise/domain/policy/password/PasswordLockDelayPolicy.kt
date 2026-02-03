package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetPasswordLockDelayUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetPasswordLockDelayUseCase

/**
 * Policy to enforce password lock delay (grace period).
 *
 * STIG V-268926 requires limiting the time before password is required after screen off.
 * When enabled, sets the delay to [STIG_LOCK_DELAY_SECONDS] (5 seconds).
 * When disabled, sets a more permissive delay.
 *
 * This ensures the device locks quickly after the screen turns off.
 */
@PolicyDefinition(
    title = "Password Lock Delay",
    description = "Set password lock delay per STIG V-268926. When enabled, requires password within 5 seconds of screen off.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class PasswordLockDelayPolicy : BooleanStatePolicy() {

    private val getUseCase = GetPasswordLockDelayUseCase()
    private val setUseCase = SetPasswordLockDelayUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getUseCase()) {
            is ApiResult.Success -> {
                // Enabled if delay is set and meets STIG requirement (<= 5 seconds)
                val meetsRequirement = result.data in 0..STIG_LOCK_DELAY_SECONDS
                ApiResult.Success(meetsRequirement)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        val value = if (enabled) STIG_LOCK_DELAY_SECONDS else DISABLED_VALUE
        return when (val result = setUseCase(value)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    companion object {
        /** STIG V-268926 requires lock within 5 seconds */
        const val STIG_LOCK_DELAY_SECONDS = 5
        /** Permissive value - 5 minutes */
        const val DISABLED_VALUE = 300
    }
}
