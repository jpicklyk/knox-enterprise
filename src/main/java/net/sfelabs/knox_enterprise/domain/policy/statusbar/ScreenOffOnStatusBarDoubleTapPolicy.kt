package net.sfelabs.knox_enterprise.domain.policy.statusbar

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.GetScreenOffOnStatusBarDoubleTapStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.SetScreenOffOnStatusBarDoubleTapStateUseCase

@PolicyDefinition(
    title = "Screen Off Double Tap",
    description = "Enable or disable turning off the screen by double-tapping the status bar.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class ScreenOffOnStatusBarDoubleTapPolicy : BooleanStatePolicy() {
    private val getUseCase = GetScreenOffOnStatusBarDoubleTapStateUseCase()
    private val setUseCase = SetScreenOffOnStatusBarDoubleTapStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
