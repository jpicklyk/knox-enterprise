package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.EnableLimitNumberOfSmsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsLimitNumberOfSmsEnabledUseCase

@PolicyDefinition(
    title = "Limit Number of SMS",
    description = "Enable or disable limiting the number of SMS messages that can be sent from the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class LimitNumberOfSmsEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsLimitNumberOfSmsEnabledUseCase()
    private val setUseCase = EnableLimitNumberOfSmsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
