package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.AllowIncomingMmsUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsIncomingMmsAllowedUseCase

@PolicyDefinition(
    title = "Block Incoming MMS",
    description = "When enabled, blocks receiving incoming MMS messages on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class AllowIncomingMmsPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsIncomingMmsAllowedUseCase()
    private val setUseCase = AllowIncomingMmsUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
