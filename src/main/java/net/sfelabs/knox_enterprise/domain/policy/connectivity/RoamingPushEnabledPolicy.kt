package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsRoamingPushEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetRoamingPushUseCase

@PolicyDefinition(
    title = "Roaming Push Enabled",
    description = "Enable or disable push notifications while roaming.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.REQUIRES_SIM
    ]
)
class RoamingPushEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsRoamingPushEnabledUseCase()
    private val setUseCase = SetRoamingPushUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
