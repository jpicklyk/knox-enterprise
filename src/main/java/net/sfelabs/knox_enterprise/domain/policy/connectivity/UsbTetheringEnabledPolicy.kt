package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsUsbTetheringEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetUsbTetheringEnabledUseCase

@PolicyDefinition(
    title = "USB Tethering Enabled",
    description = "Enable or disable USB tethering functionality on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.AFFECTS_CONNECTIVITY
    ]
)
class UsbTetheringEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsUsbTetheringEnabledUseCase()
    private val setUseCase = SetUsbTetheringEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
