package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsBluetoothTetheringEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetBluetoothTetheringEnabledUseCase

@PolicyDefinition(
    title = "Bluetooth Tethering Enabled",
    description = "Enable or disable Bluetooth tethering functionality on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_BLUETOOTH,
        PolicyCapability.AFFECTS_CONNECTIVITY
    ]
)
class BluetoothTetheringEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsBluetoothTetheringEnabledUseCase()
    private val setUseCase = SetBluetoothTetheringEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
