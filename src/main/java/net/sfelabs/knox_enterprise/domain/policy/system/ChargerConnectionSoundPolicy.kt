package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetChargerConnectionSoundEnabledStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetChargerConnectionSoundEnabledStateUseCase

@PolicyDefinition(
    title = "Disable Charger Connection Sound",
    description = "When enabled, disables the sound played when a charger is connected.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.MODIFIES_CHARGING,
    ]
)
class ChargerConnectionSoundPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetChargerConnectionSoundEnabledStateUseCase()
    private val setUseCase = SetChargerConnectionSoundEnabledStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
