package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.AllowOutgoingSmsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsOutgoingSmsAllowedUseCase

@PolicyDefinition(
    title = "Block Outgoing SMS",
    description = "When enabled, blocks sending outgoing SMS messages from the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class AllowOutgoingSmsPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsOutgoingSmsAllowedUseCase()
    private val setUseCase = AllowOutgoingSmsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
