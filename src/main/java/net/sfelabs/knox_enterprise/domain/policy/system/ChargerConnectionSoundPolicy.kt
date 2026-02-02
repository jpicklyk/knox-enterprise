package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetChargerConnectionSoundEnabledStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetChargerConnectionSoundEnabledStateUseCase

@PolicyDefinition(
    title = "Charger Connection Sound",
    description = "Enable or disable the sound played when a charger is connected.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.MODIFIES_CHARGING,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class ChargerConnectionSoundPolicy : BooleanStatePolicy() {
    private val getUseCase = GetChargerConnectionSoundEnabledStateUseCase()
    private val setUseCase = SetChargerConnectionSoundEnabledStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
