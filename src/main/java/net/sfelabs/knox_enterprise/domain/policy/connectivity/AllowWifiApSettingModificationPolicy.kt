package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowWifiApSettingModificationUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsWifiApSettingModificationAllowedUseCase

@PolicyDefinition(
    title = "Block WiFi AP Setting Modification",
    description = "When enabled, prevents users from modifying WiFi access point settings.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI
    ]
)
class AllowWifiApSettingModificationPolicy : BooleanStatePolicy() {
    private val getUseCase = IsWifiApSettingModificationAllowedUseCase()
    private val setUseCase = AllowWifiApSettingModificationUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
