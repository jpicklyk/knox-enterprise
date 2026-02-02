package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowBleUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsBleAllowedUseCase

@PolicyDefinition(
    title = "Allow BLE",
    description = "Allow or disallow Bluetooth Low Energy (BLE) functionality.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_BLUETOOTH,
        PolicyCapability.AFFECTS_CONNECTIVITY
    ]
)
class AllowBlePolicy : BooleanStatePolicy() {
    private val getUseCase = IsBleAllowedUseCase()
    private val setUseCase = AllowBleUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
