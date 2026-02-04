package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.GetAllowUserWifiProfilesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetAllowUserWifiProfilesUseCase

@PolicyDefinition(
    title = "Block User WiFi Profiles",
    description = "When enabled, prevents users from creating and modifying WiFi network profiles.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
    ]
)
class AllowUserWifiProfilesPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetAllowUserWifiProfilesUseCase()
    private val setUseCase = SetAllowUserWifiProfilesUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
