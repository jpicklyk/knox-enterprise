package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsPasswordVisibilityEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetPasswordVisibilityEnabledUseCase

@PolicyDefinition(
    title = "Password Visibility Enabled",
    description = "Enable or disable showing password characters briefly while typing.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.MODIFIES_DISPLAY
    ]
)
class PasswordVisibilityEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsPasswordVisibilityEnabledUseCase()
    private val setUseCase = SetPasswordVisibilityEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
