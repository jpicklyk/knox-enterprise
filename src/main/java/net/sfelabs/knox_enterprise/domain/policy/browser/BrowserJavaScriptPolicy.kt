package net.sfelabs.knox_enterprise.domain.policy.browser

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.browser.GetJavaScriptSettingUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.browser.SetJavaScriptSettingUseCase

@PolicyDefinition(
    title = "Browser JavaScript",
    description = "Enable or disable JavaScript execution in the Samsung Internet browser.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class BrowserJavaScriptPolicy : BooleanStatePolicy() {
    private val getUseCase = GetJavaScriptSettingUseCase()
    private val setUseCase = SetJavaScriptSettingUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        setUseCase(enabled)
        return ApiResult.Success(Unit)
    }
}
