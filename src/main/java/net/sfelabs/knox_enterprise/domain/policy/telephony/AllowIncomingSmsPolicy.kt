package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.AllowIncomingSmsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsIncomingSmsAllowedUseCase

@PolicyDefinition(
    title = "Block Incoming SMS",
    description = "When enabled, blocks receiving incoming SMS messages on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class AllowIncomingSmsPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsIncomingSmsAllowedUseCase()
    private val setUseCase = AllowIncomingSmsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
