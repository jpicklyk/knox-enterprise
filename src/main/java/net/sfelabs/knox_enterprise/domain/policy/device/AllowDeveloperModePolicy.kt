package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowDeveloperModeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsDeveloperModeAllowedUseCase

@PolicyDefinition(
    title = "Allow Developer Mode",
    description = "Allow or disallow the user from enabling developer options on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class AllowDeveloperModePolicy : BooleanStatePolicy() {
    private val getUseCase = IsDeveloperModeAllowedUseCase()
    private val setUseCase = AllowDeveloperModeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
