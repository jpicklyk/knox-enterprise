package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.AllowClipboardShareUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsClipboardShareAllowedUseCase

@PolicyDefinition(
    title = "Allow Clipboard Share",
    description = "Allow or disallow sharing clipboard content between applications.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class AllowClipboardSharePolicy : BooleanStatePolicy() {
    private val getUseCase = IsClipboardShareAllowedUseCase()
    private val setUseCase = AllowClipboardShareUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
