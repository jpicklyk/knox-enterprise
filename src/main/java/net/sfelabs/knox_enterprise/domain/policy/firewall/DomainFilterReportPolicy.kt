package net.sfelabs.knox_enterprise.domain.policy.firewall

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.firewall.EnableDomainFilterReportUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.firewall.IsDomainFilterReportEnabledUseCase

@PolicyDefinition(
    title = "Domain Filter Report",
    description = "Enable or disable domain filter reporting for tracking blocked domains.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class DomainFilterReportPolicy : BooleanStatePolicy() {
    private val getUseCase = IsDomainFilterReportEnabledUseCase()
    private val setUseCase = EnableDomainFilterReportUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
