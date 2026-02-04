package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowPowerOffUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsPowerOffAllowedUseCase

@PolicyDefinition(
    title = "Block Power Off",
    description = "When enabled, prevents users from powering off the device using the power button.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowPowerOffPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsPowerOffAllowedUseCase()
    private val setUseCase = AllowPowerOffUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
