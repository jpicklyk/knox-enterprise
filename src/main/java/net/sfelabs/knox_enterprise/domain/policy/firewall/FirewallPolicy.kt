package net.sfelabs.knox_enterprise.domain.policy.firewall

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.firewall.EnableFirewallUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.firewall.IsFirewallEnabledUseCase

@PolicyDefinition(
    title = "Firewall",
    description = "Enable or disable the Knox firewall for network traffic control.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.MODIFIES_SECURITY,
    ]
)
class FirewallPolicy : BooleanStatePolicy() {
    private val getUseCase = IsFirewallEnabledUseCase()
    private val setUseCase = EnableFirewallUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
