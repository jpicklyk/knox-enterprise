package net.sfelabs.knox_enterprise.domain.policy.media

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.media.IsMicrophoneEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.media.SetMicrophoneEnabledUseCase

@PolicyDefinition(
    title = "Microphone Enabled",
    description = "Enable or disable the device microphone.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.EASILY_REVERSIBLE,
        PolicyCapability.STIG
    ]
)
class MicrophoneEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsMicrophoneEnabledUseCase()
    private val setUseCase = SetMicrophoneEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
