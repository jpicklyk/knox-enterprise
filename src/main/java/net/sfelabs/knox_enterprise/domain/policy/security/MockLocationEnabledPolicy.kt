package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsMockLocationEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetMockLocationEnabledUseCase

@PolicyDefinition(
    title = "Disable Mock Location",
    description = "When enabled, disables mock location (fake GPS) functionality.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class MockLocationEnabledPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsMockLocationEnabledUseCase()
    private val setUseCase = SetMockLocationEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
