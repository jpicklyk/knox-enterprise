package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowFactoryResetUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsFactoryResetAllowedUseCase

@PolicyDefinition(
    title = "Allow Factory Reset",
    description = "Allow or disallow the user from performing a factory reset on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.PERSISTENT_ACROSS_REBOOT
    ]
)
class AllowFactoryResetPolicy : BooleanStatePolicy() {
    private val getUseCase = IsFactoryResetAllowedUseCase()
    private val setUseCase = AllowFactoryResetUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
