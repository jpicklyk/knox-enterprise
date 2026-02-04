package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowBackgroundProcessLimitUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsBackgroundProcessLimitAllowedUseCase

@PolicyDefinition(
    title = "Block Background Process Limit",
    description = "When enabled, prevents users from setting background process limits in developer options.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.AFFECTS_BATTERY
    ]
)
class AllowBackgroundProcessLimitPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsBackgroundProcessLimitAllowedUseCase()
    private val setUseCase = AllowBackgroundProcessLimitUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
