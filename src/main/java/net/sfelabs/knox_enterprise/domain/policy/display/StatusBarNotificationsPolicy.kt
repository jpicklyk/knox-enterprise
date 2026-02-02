package net.sfelabs.knox_enterprise.domain.policy.display

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.display.GetStatusBarNotificationsStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.display.SetStatusBarNotificationsStateUseCase

@PolicyDefinition(
    title = "Status Bar Notifications",
    description = "Show or hide notifications in the status bar.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class StatusBarNotificationsPolicy : BooleanStatePolicy() {
    private val getUseCase = GetStatusBarNotificationsStateUseCase()
    private val setUseCase = SetStatusBarNotificationsStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
