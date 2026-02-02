package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsRoamingVoiceCallsEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetRoamingVoiceCallsUseCase

@PolicyDefinition(
    title = "Roaming Voice Calls Enabled",
    description = "Enable or disable voice calls while roaming.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_RADIO,
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class RoamingVoiceCallsEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsRoamingVoiceCallsEnabledUseCase()
    private val setUseCase = SetRoamingVoiceCallsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
