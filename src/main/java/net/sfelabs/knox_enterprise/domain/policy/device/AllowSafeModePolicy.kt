package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowSafeModeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsSafeModeAllowedUseCase

@PolicyDefinition(
    title = "Block Safe Mode",
    description = "When enabled, prevents users from booting the device into safe mode.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowSafeModePolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsSafeModeAllowedUseCase()
    private val setUseCase = AllowSafeModeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
