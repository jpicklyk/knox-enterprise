package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.AllowCopyContactToSimUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsCopyContactToSimAllowedUseCase

@PolicyDefinition(
    title = "Block Copy Contact To SIM",
    description = "When enabled, prevents copying contacts to the SIM card.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class AllowCopyContactToSimPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsCopyContactToSimAllowedUseCase()
    private val setUseCase = AllowCopyContactToSimUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
