package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowDataSavingUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsDataSavingAllowedUseCase

@PolicyDefinition(
    title = "Allow Data Saving",
    description = "Allow or disallow the user from enabling the Data Saver feature.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.AFFECTS_BATTERY
    ]
)
class AllowDataSavingPolicy : BooleanStatePolicy() {
    private val getUseCase = IsDataSavingAllowedUseCase()
    private val setUseCase = AllowDataSavingUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
