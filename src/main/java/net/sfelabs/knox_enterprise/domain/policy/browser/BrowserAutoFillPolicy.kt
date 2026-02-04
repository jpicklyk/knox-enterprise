package net.sfelabs.knox_enterprise.domain.policy.browser

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.browser.GetAutoFillSettingUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.browser.SetAutoFillSettingUseCase

@PolicyDefinition(
    title = "Disable Browser Auto-Fill",
    description = "When enabled, disables form auto-fill in the Samsung Internet browser.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_BROWSER,
        PolicyCapability.MODIFIES_SECURITY,
    ]
)
class BrowserAutoFillPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetAutoFillSettingUseCase()
    private val setUseCase = SetAutoFillSettingUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        setUseCase(enabled)
        return ApiResult.Success(Unit)
    }
}
