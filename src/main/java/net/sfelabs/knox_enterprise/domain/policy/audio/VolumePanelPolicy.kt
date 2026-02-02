package net.sfelabs.knox_enterprise.domain.policy.audio

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.audio.GetVolumePanelEnabledStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.audio.SetVolumePanelEnabledStateUseCase

@PolicyDefinition(
    title = "Volume Panel",
    description = "Enable or disable the system volume panel for adjusting audio settings.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class VolumePanelPolicy : BooleanStatePolicy() {
    private val getUseCase = GetVolumePanelEnabledStateUseCase()
    private val setUseCase = SetVolumePanelEnabledStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
