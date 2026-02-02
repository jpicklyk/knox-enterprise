package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsRoamingSyncEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetRoamingSyncUseCase

@PolicyDefinition(
    title = "Roaming Sync Enabled",
    description = "Enable or disable data synchronization while roaming.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.REQUIRES_SIM
    ]
)
class RoamingSyncEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsRoamingSyncEnabledUseCase()
    private val setUseCase = SetRoamingSyncUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
