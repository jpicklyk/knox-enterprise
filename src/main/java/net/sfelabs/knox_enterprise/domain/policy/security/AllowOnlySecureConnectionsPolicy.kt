package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.AllowOnlySecureConnectionsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsOnlySecureConnectionsAllowedUseCase

@PolicyDefinition(
    title = "Require Secure Connections",
    description = "When enabled, requires all network connections to use secure protocols (HTTPS, TLS).",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowOnlySecureConnectionsPolicy : BooleanStatePolicy() {
    private val getUseCase = IsOnlySecureConnectionsAllowedUseCase()
    private val setUseCase = AllowOnlySecureConnectionsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
