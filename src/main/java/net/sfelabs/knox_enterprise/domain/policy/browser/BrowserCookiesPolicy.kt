package net.sfelabs.knox_enterprise.domain.policy.browser

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.browser.GetCookiesSettingUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.browser.SetCookiesSettingUseCase

@PolicyDefinition(
    title = "Disable Browser Cookies",
    description = "When enabled, disables cookies in the Samsung Internet browser.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_BROWSER,
        PolicyCapability.MODIFIES_SECURITY,
    ]
)
class BrowserCookiesPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetCookiesSettingUseCase()
    private val setUseCase = SetCookiesSettingUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        setUseCase(enabled)
        return ApiResult.Success(Unit)
    }
}
