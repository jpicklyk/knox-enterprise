package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetTorchOnVolumeButtonsStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetTorchOnVolumeButtonsStateUseCase

@PolicyDefinition(
    title = "Torch on Volume Buttons",
    description = "Enable or disable torch activation via volume buttons.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class TorchOnVolumeButtonsPolicy : BooleanStatePolicy() {
    private val getUseCase = GetTorchOnVolumeButtonsStateUseCase()
    private val setUseCase = SetTorchOnVolumeButtonsStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
