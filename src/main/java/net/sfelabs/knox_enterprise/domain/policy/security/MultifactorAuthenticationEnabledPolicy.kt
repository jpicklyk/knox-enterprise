package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsMultifactorAuthenticationEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetMultifactorAuthenticationEnabledUseCase

@PolicyDefinition(
    title = "Multifactor Authentication Enabled",
    description = "Enable or disable multifactor authentication requirements.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class MultifactorAuthenticationEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsMultifactorAuthenticationEnabledUseCase()
    private val setUseCase = SetMultifactorAuthenticationEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
