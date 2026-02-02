package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.GetAllowUserWifiProfilesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetAllowUserWifiProfilesUseCase

@PolicyDefinition(
    title = "Allow User WiFi Profiles",
    description = "Allow or disallow the user from creating and modifying WiFi network profiles.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class AllowUserWifiProfilesPolicy : BooleanStatePolicy() {
    private val getUseCase = GetAllowUserWifiProfilesUseCase()
    private val setUseCase = SetAllowUserWifiProfilesUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
