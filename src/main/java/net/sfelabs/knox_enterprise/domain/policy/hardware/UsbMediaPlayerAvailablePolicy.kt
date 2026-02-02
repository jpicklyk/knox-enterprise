package net.sfelabs.knox_enterprise.domain.policy.hardware

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.IsUsbMediaPlayerAvailableUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.SetUsbMediaPlayerAvailableUseCase

@PolicyDefinition(
    title = "USB Media Player Available",
    description = "Enable or disable USB media player functionality.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class UsbMediaPlayerAvailablePolicy : BooleanStatePolicy() {
    private val getUseCase = IsUsbMediaPlayerAvailableUseCase()
    private val setUseCase = SetUsbMediaPlayerAvailableUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
