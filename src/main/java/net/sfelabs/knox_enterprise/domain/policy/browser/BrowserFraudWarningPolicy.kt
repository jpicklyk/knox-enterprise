package net.sfelabs.knox_enterprise.domain.policy.browser

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.browser.GetForceFraudWarningSettingUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.browser.SetForceFraudWarningSettingUseCase

@PolicyDefinition(
    title = "Browser Fraud Warning",
    description = "Force fraud warnings in the Samsung Internet browser to protect users from malicious websites.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_BROWSER,
        PolicyCapability.MODIFIES_SECURITY,
    ]
)
class BrowserFraudWarningPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetForceFraudWarningSettingUseCase()
    private val setUseCase = SetForceFraudWarningSettingUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        setUseCase(enabled)
        return ApiResult.Success(Unit)
    }
}
