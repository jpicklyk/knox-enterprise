package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsCellularDataAllowedUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetCellularDataEnabledUseCase

@PolicyDefinition(
    title = "Disable Cellular Data",
    description = "When enabled, disables cellular data connectivity on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_RADIO,
        PolicyCapability.AFFECTS_CONNECTIVITY,
        PolicyCapability.REQUIRES_SIM
    ]
)
class CellularDataEnabledPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsCellularDataAllowedUseCase()
    private val setUseCase = SetCellularDataEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
