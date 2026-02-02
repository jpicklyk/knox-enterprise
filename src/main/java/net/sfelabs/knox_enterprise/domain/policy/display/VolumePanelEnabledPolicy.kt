package net.sfelabs.knox_enterprise.domain.policy.display

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.display.GetVolumePanelEnabledStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.display.SetVolumePanelEnabledStateUseCase

@PolicyDefinition(
    title = "Volume Panel",
    description = "Enable or disable the volume panel display on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class VolumePanelEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = GetVolumePanelEnabledStateUseCase()
    private val setUseCase = SetVolumePanelEnabledStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
