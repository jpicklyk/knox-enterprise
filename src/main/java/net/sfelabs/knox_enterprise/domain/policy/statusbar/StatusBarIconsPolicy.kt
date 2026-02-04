package net.sfelabs.knox_enterprise.domain.policy.statusbar

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.GetStatusBarIconsStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.SetStatusBarIconsStateUseCase

@PolicyDefinition(
    title = "Hide Status Bar Icons",
    description = "When enabled, hides icons in the status bar.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
    ]
)
class StatusBarIconsPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetStatusBarIconsStateUseCase()
    private val setUseCase = SetStatusBarIconsStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
