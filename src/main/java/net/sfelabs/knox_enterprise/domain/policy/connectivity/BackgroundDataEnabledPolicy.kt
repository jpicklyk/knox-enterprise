package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsBackgroundDataEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetBackgroundDataEnabledUseCase

@PolicyDefinition(
    title = "Background Data Enabled",
    description = "Enable or disable background data usage for applications.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.AFFECTS_BATTERY,
        PolicyCapability.AFFECTS_CONNECTIVITY
    ]
)
class BackgroundDataEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsBackgroundDataEnabledUseCase()
    private val setUseCase = SetBackgroundDataEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
