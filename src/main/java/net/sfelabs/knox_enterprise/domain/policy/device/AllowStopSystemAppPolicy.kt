package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowStopSystemAppUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsStopSystemAppAllowedUseCase

@PolicyDefinition(
    title = "Allow Stop System App",
    description = "Allow or disallow the user from force stopping system applications.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY
    ]
)
class AllowStopSystemAppPolicy : BooleanStatePolicy() {
    private val getUseCase = IsStopSystemAppAllowedUseCase()
    private val setUseCase = AllowStopSystemAppUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
