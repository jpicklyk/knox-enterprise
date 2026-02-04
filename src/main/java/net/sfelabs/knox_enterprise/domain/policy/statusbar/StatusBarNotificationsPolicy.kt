package net.sfelabs.knox_enterprise.domain.policy.statusbar

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.GetStatusBarNotificationsStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.statusbar.SetStatusBarNotificationsStateUseCase

@PolicyDefinition(
    title = "Hide Status Bar Notifications",
    description = "When enabled, hides notifications in the status bar.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
    ]
)
class StatusBarNotificationsPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetStatusBarNotificationsStateUseCase()
    private val setUseCase = SetStatusBarNotificationsStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
