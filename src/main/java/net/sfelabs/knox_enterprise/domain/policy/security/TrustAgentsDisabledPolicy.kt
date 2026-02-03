package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.admin.GetActiveAdminComponentUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.AreTrustAgentsDisabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetTrustAgentsDisabledUseCase

/**
 * Policy to disable trust agents (Smart Lock features).
 *
 * STIG V-268927 requires disabling trust agents to ensure the screen-lock policy
 * is properly enforced without bypass mechanisms.
 *
 * Trust agents include:
 * - Smart Lock (trusted places, trusted devices, on-body detection)
 * - Voice Match unlock
 * - Other lock screen bypass mechanisms
 *
 * When enabled (STIG compliant), trust agents are disabled.
 * When disabled, trust agents are allowed.
 */
@PolicyDefinition(
    title = "Disable Trust Agents",
    description = "Disable Smart Lock and trust agents per STIG V-268927. When enabled, prevents lock screen bypass.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class TrustAgentsDisabledPolicy : BooleanStatePolicy() {

    private val getAdminUseCase = GetActiveAdminComponentUseCase()
    private val areTrustAgentsDisabledUseCase = AreTrustAgentsDisabledUseCase()
    private val setTrustAgentsDisabledUseCase = SetTrustAgentsDisabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        // Get admin component
        val admin = when (val adminResult = getAdminUseCase()) {
            is ApiResult.Success -> adminResult.data
            is ApiResult.Error -> return ApiResult.Error(adminResult.apiError, adminResult.exception)
            ApiResult.NotSupported -> return ApiResult.NotSupported
        }

        // Return whether trust agents are disabled (which means STIG is enabled)
        return when (val result = areTrustAgentsDisabledUseCase(admin)) {
            is ApiResult.Success -> ApiResult.Success(result.data)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        // Get admin component
        val admin = when (val adminResult = getAdminUseCase()) {
            is ApiResult.Success -> adminResult.data
            is ApiResult.Error -> return ApiResult.Error(adminResult.apiError, adminResult.exception)
            ApiResult.NotSupported -> return ApiResult.NotSupported
        }

        val params = SetTrustAgentsDisabledUseCase.Params(
            adminComponent = admin,
            disabled = enabled  // enabled = trust agents disabled = STIG compliant
        )
        return when (val result = setTrustAgentsDisabledUseCase(params)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
