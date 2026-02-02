package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetPowerMenuLockedStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetPowerMenuLockedStateUseCase

@PolicyDefinition(
    title = "Power Menu Locked",
    description = "Lock or unlock the power menu options.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class PowerMenuLockedPolicy : BooleanStatePolicy() {
    private val getUseCase = GetPowerMenuLockedStateUseCase()
    private val setUseCase = SetPowerMenuLockedStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
