package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetVolumeButtonRotationStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetVolumeButtonRotationStateUseCase

@PolicyDefinition(
    title = "Volume Button Rotation",
    description = "Enable or disable volume button rotation with screen orientation.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.MODIFIES_DISPLAY,
    ]
)
class VolumeButtonRotationPolicy : BooleanStatePolicy() {
    private val getUseCase = GetVolumeButtonRotationStateUseCase()
    private val setUseCase = SetVolumeButtonRotationStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
