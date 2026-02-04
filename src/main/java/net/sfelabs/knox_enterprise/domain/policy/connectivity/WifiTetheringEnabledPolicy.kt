package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsWifiTetheringEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetWifiTetheringEnabledUseCase

@PolicyDefinition(
    title = "Disable WiFi Tethering",
    description = "When enabled, disables WiFi hotspot tethering functionality on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.AFFECTS_CONNECTIVITY,
        PolicyCapability.STIG
    ]
)
class WifiTetheringEnabledPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsWifiTetheringEnabledUseCase()
    private val setUseCase = SetWifiTetheringEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
