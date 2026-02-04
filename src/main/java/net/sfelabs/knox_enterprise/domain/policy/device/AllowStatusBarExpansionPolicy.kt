package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowStatusBarExpansionUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsStatusBarExpansionAllowedUseCase

@PolicyDefinition(
    title = "Block Status Bar Expansion",
    description = "When enabled, prevents users from expanding the notification panel.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
    ]
)
class AllowStatusBarExpansionPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsStatusBarExpansionAllowedUseCase()
    private val setUseCase = AllowStatusBarExpansionUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
