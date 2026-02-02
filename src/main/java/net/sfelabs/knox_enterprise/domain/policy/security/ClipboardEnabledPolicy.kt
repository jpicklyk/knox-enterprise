package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsClipboardAllowedUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetClipboardEnabledUseCase

@PolicyDefinition(
    title = "Clipboard Enabled",
    description = "Enable or disable clipboard functionality on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class ClipboardEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsClipboardAllowedUseCase()
    private val setUseCase = SetClipboardEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
