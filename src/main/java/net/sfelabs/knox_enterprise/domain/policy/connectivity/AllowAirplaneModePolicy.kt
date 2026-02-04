package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowAirplaneModeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsAirplaneModeAllowedUseCase

@PolicyDefinition(
    title = "Block Airplane Mode",
    description = "When enabled, prevents users from toggling airplane mode on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_RADIO,
        PolicyCapability.AFFECTS_CONNECTIVITY,
    ]
)
class AllowAirplaneModePolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsAirplaneModeAllowedUseCase()
    private val setUseCase = AllowAirplaneModeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
