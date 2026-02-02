package net.sfelabs.knox_enterprise.domain.policy.browser

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.browser.GetPopupsSettingUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.browser.SetPopupsSettingUseCase

@PolicyDefinition(
    title = "Browser Popups",
    description = "Allow or block popup windows in the Samsung Internet browser.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class BrowserPopupsPolicy : BooleanStatePolicy() {
    private val getUseCase = GetPopupsSettingUseCase()
    private val setUseCase = SetPopupsSettingUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        setUseCase(enabled)
        return ApiResult.Success(Unit)
    }
}
