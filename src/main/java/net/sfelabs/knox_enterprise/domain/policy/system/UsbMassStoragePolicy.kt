package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetUsbMassStorageStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetUsbMassStorageStateUseCase

@PolicyDefinition(
    title = "USB Mass Storage",
    description = "Enable or disable USB mass storage mode.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class UsbMassStoragePolicy : BooleanStatePolicy() {
    private val getUseCase = GetUsbMassStorageStateUseCase()
    private val setUseCase = SetUsbMassStorageStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
