package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsScreenLockPatternVisibilityEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetScreenLockPatternVisibilityUseCase

@PolicyDefinition(
    title = "Screen Lock Pattern Visibility",
    description = "Enable or disable showing the pattern trail when drawing the unlock pattern.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.MODIFIES_DISPLAY
    ]
)
class ScreenLockPatternVisibilityPolicy : BooleanStatePolicy() {
    private val getUseCase = IsScreenLockPatternVisibilityEnabledUseCase()
    private val setUseCase = SetScreenLockPatternVisibilityUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
