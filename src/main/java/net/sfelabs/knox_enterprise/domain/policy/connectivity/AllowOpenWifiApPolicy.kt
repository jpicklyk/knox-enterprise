package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowOpenWifiApUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsOpenWifiApAllowedUseCase

@PolicyDefinition(
    title = "Block Open WiFi AP",
    description = "When enabled, prevents creation of open (unsecured) WiFi access points.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowOpenWifiApPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsOpenWifiApAllowedUseCase()
    private val setUseCase = AllowOpenWifiApUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
