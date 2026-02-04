package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowSmartClipModeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsSmartClipModeAllowedUseCase

@PolicyDefinition(
    title = "Block Smart Clip Mode",
    description = "When enabled, disables the stylus smart clip feature on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
    ]
)
class AllowSmartClipModePolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsSmartClipModeAllowedUseCase()
    private val setUseCase = AllowSmartClipModeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
