package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.GetWifiPasswordHiddenUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetWifiPasswordHiddenUseCase

@PolicyDefinition(
    title = "WiFi Password Hidden",
    description = "Hide or show WiFi passwords in the WiFi settings UI.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.MODIFIES_DISPLAY
    ]
)
class WifiPasswordHiddenPolicy : BooleanStatePolicy() {
    private val getUseCase = GetWifiPasswordHiddenUseCase()
    private val setUseCase = SetWifiPasswordHiddenUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
