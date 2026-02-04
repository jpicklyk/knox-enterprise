package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsScreenLockPatternVisibilityEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetScreenLockPatternVisibilityUseCase

@PolicyDefinition(
    title = "Hide Screen Lock Pattern",
    description = "When enabled, hides the pattern trail when drawing the unlock pattern.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.MODIFIES_DISPLAY
    ]
)
class ScreenLockPatternVisibilityPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsScreenLockPatternVisibilityEnabledUseCase()
    private val setUseCase = SetScreenLockPatternVisibilityUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
