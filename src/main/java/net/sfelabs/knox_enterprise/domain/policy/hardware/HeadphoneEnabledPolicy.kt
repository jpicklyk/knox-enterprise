package net.sfelabs.knox_enterprise.domain.policy.hardware

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.IsHeadphoneEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.SetHeadphoneEnabledUseCase

@PolicyDefinition(
    title = "Headphone Enabled",
    description = "Enable or disable headphone audio output on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_AUDIO,
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class HeadphoneEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsHeadphoneEnabledUseCase()
    private val setUseCase = SetHeadphoneEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
