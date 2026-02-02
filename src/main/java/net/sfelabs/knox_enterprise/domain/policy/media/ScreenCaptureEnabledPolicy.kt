package net.sfelabs.knox_enterprise.domain.policy.media

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.media.IsScreenCaptureEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.media.SetScreenCaptureEnabledUseCase

@PolicyDefinition(
    title = "Screen Capture Enabled",
    description = "Enable or disable screen capture (screenshots) on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class ScreenCaptureEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsScreenCaptureEnabledUseCase()
    private val setUseCase = SetScreenCaptureEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
