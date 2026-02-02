package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.ActivateWifiSsidRestrictionUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsWifiSsidRestrictionActiveUseCase

@PolicyDefinition(
    title = "WiFi SSID Restriction Active",
    description = "Activate or deactivate WiFi SSID-based network restrictions.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class WifiSsidRestrictionActivePolicy : BooleanStatePolicy() {
    private val getUseCase = IsWifiSsidRestrictionActiveUseCase()
    private val setUseCase = ActivateWifiSsidRestrictionUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
