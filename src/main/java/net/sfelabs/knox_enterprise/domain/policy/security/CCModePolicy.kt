package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.model.CCModeState
import net.sfelabs.knox_enterprise.domain.use_cases.GetCCModeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.SetCCModeUseCase

/**
 * Policy to control Common Criteria (CC) Mode.
 *
 * STIG V-268966 requires enabling CC mode for work profile.
 * STIG V-268969 (CRL checking) is automatically enforced when CC mode is enabled.
 *
 * When CC mode is enabled:
 * - Certificate Revocation List (CRL) checking is enforced
 * - Enhanced security validations are applied
 * - Certain insecure features are disabled
 */
@PolicyDefinition(
    title = "Common Criteria (CC) Mode",
    description = "Enable or disable Common Criteria mode. When enabled, enforces CRL checking and enhanced security validations.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class CCModePolicy : BooleanStatePolicy() {
    private val getUseCase = GetCCModeUseCase()
    private val setUseCase = SetCCModeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return getUseCase().map { state ->
            state == CCModeState.ENABLED
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
