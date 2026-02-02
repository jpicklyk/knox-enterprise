package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetDeviceSpeakerEnabledStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetDeviceSpeakerEnabledStateUseCase

@PolicyDefinition(
    title = "Device Speaker",
    description = "Enable or disable the device speaker.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class DeviceSpeakerEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = GetDeviceSpeakerEnabledStateUseCase()
    private val setUseCase = SetDeviceSpeakerEnabledStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
