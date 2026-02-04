package net.sfelabs.knox_enterprise.domain.policy.firewall

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.firewall.EnableDomainFilterOnIptablesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.firewall.IsDomainFilterOnIptablesEnabledUseCase

@PolicyDefinition(
    title = "Domain Filter on IPTables",
    description = "Enable or disable domain filtering using IPTables for low-level network filtering.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.MODIFIES_SECURITY,
    ]
)
class DomainFilterOnIptablesPolicy : BooleanStatePolicy() {
    private val getUseCase = IsDomainFilterOnIptablesEnabledUseCase()
    private val setUseCase = EnableDomainFilterOnIptablesUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
