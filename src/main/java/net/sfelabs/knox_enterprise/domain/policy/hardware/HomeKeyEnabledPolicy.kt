package net.sfelabs.knox_enterprise.domain.policy.hardware

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.IsHomeKeyEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.SetHomeKeyEnabledUseCase

@PolicyDefinition(
    title = "Home Key Enabled",
    description = "Enable or disable the home key/button on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class HomeKeyEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsHomeKeyEnabledUseCase()
    private val setUseCase = SetHomeKeyEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
