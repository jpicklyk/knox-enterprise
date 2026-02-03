package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.security.AllowFirmwareAutoUpdateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsFirmwareAutoUpdateAllowedUseCase

@PolicyDefinition(
    title = "Block Firmware Auto Update",
    description = "When enabled, prevents automatic firmware (OS) updates.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.PERSISTENT_ACROSS_REBOOT
    ]
)
class AllowFirmwareAutoUpdatePolicy : BooleanStatePolicy() {
    private val getUseCase = IsFirmwareAutoUpdateAllowedUseCase()
    private val setUseCase = AllowFirmwareAutoUpdateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
