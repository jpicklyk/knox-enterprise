package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowWifiDirectUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsWifiDirectAllowedUseCase

@PolicyDefinition(
    title = "Allow WiFi Direct",
    description = "Allow or disallow the user from using WiFi Direct for peer-to-peer connections.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.AFFECTS_CONNECTIVITY,
        PolicyCapability.EASILY_REVERSIBLE,
        PolicyCapability.STIG
    ]
)
class AllowWifiDirectPolicy : BooleanStatePolicy() {
    private val getUseCase = IsWifiDirectAllowedUseCase()
    private val setUseCase = AllowWifiDirectUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
