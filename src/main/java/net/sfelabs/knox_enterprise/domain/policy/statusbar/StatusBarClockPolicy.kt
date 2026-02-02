package net.sfelabs.knox_enterprise.domain.policy.statusbar

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.GetStatusBarClockStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.SetStatusBarClockStateUseCase

@PolicyDefinition(
    title = "Status Bar Clock",
    description = "Show or hide the clock in the status bar.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class StatusBarClockPolicy : BooleanStatePolicy() {
    private val getUseCase = GetStatusBarClockStateUseCase()
    private val setUseCase = SetStatusBarClockStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
