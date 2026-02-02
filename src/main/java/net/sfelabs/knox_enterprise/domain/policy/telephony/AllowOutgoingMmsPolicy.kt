package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.AllowOutgoingMmsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsOutgoingMmsAllowedUseCase

@PolicyDefinition(
    title = "Allow Outgoing MMS",
    description = "Allow or disallow sending outgoing MMS messages from the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class AllowOutgoingMmsPolicy : BooleanStatePolicy() {
    private val getUseCase = IsOutgoingMmsAllowedUseCase()
    private val setUseCase = AllowOutgoingMmsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
