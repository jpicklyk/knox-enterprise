package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowWifiScanningUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsWifiScanningAllowedUseCase

@PolicyDefinition(
    title = "Block WiFi Scanning",
    description = "When enabled, blocks WiFi scanning for network discovery.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.AFFECTS_CONNECTIVITY
    ]
)
class AllowWifiScanningPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsWifiScanningAllowedUseCase()
    private val setUseCase = AllowWifiScanningUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
