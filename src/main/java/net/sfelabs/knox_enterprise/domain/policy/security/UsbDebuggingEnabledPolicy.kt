package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsUsbDebuggingEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.SetUsbDebuggingEnabledUseCase

@PolicyDefinition(
    title = "Disable USB Debugging",
    description = "When enabled, disables USB debugging (ADB) on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class UsbDebuggingEnabledPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsUsbDebuggingEnabledUseCase()
    private val setUseCase = SetUsbDebuggingEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
