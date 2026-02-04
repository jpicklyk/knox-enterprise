package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsPasswordVisibilityEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetPasswordVisibilityEnabledUseCase

@PolicyDefinition(
    title = "Disable Password Visibility",
    description = "When enabled, hides password characters while typing (no brief reveal).",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.STIG
    ]
)
class PasswordVisibilityEnabledPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsPasswordVisibilityEnabledUseCase()
    private val setUseCase = SetPasswordVisibilityEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
