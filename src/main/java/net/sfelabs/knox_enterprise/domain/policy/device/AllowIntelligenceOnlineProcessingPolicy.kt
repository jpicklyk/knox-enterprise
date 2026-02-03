package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowIntelligenceOnlineProcessingUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsIntelligenceOnlineProcessingAllowedUseCase

/**
 * Policy to control AI/Intelligence cloud processing.
 *
 * STIG V-268932 (CAT I - Critical) requires excluding AI apps that process data
 * in the cloud (e.g., Google Gemini, Samsung AI features).
 *
 * When disabled, AI features requiring cloud processing will be blocked.
 */
@PolicyDefinition(
    title = "Allow Intelligence Online Processing",
    description = "Allow or disallow AI/Intelligence features from processing data in the cloud. Disabling blocks cloud-based AI features like Google Gemini.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class AllowIntelligenceOnlineProcessingPolicy : BooleanStatePolicy() {
    private val getUseCase = IsIntelligenceOnlineProcessingAllowedUseCase()
    private val setUseCase = AllowIntelligenceOnlineProcessingUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
