package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowPowerSavingModeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsPowerSavingModeAllowedUseCase

@PolicyDefinition(
    title = "Block Power Saving Mode",
    description = "When enabled, prevents users from enabling power saving mode on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.AFFECTS_BATTERY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class AllowPowerSavingModePolicy : BooleanStatePolicy() {
    private val getUseCase = IsPowerSavingModeAllowedUseCase()
    private val setUseCase = AllowPowerSavingModeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
