package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.GetAutomaticWifiConnectionUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetAutomaticWifiConnectionUseCase

@PolicyDefinition(
    title = "Automatic WiFi Connection",
    description = "Enable or disable automatic connection to configured WiFi networks.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.AFFECTS_CONNECTIVITY
    ]
)
class AutomaticWifiConnectionPolicy : BooleanStatePolicy() {
    private val getUseCase = GetAutomaticWifiConnectionUseCase()
    private val setUseCase = SetAutomaticWifiConnectionUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
