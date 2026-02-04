package net.sfelabs.knox_enterprise.domain.policy.datetime

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.datetime.GetAutomaticTimeStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.datetime.SetAutomaticTimeStateUseCase

@PolicyDefinition(
    title = "Disable Automatic Time",
    description = "When enabled, disables automatic date and time synchronization from the network.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
    ]
)
class AutomaticTimePolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetAutomaticTimeStateUseCase()
    private val setUseCase = SetAutomaticTimeStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
