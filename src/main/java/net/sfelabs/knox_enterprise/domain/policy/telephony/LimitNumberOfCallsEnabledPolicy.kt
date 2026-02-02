package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.EnableLimitNumberOfCallsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsLimitNumberOfCallsEnabledUseCase

@PolicyDefinition(
    title = "Limit Number of Calls",
    description = "Enable or disable limiting the number of calls that can be made from the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class LimitNumberOfCallsEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsLimitNumberOfCallsEnabledUseCase()
    private val setUseCase = EnableLimitNumberOfCallsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
