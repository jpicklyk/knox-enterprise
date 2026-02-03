package net.sfelabs.knox_enterprise.domain.policy.hardware

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.IsSdCardEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.SetSdCardEnabledUseCase

@PolicyDefinition(
    title = "SD Card Enabled",
    description = "Enable or disable SD card access on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class SdCardEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsSdCardEnabledUseCase()
    private val setUseCase = SetSdCardEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
