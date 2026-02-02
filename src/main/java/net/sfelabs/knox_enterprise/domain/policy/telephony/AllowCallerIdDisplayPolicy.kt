package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.AllowCallerIdDisplayUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsCallerIdDisplayAllowedUseCase

@PolicyDefinition(
    title = "Allow Caller ID Display",
    description = "Allow or disallow displaying caller ID for incoming calls.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.MODIFIES_DISPLAY
    ]
)
class AllowCallerIdDisplayPolicy : BooleanStatePolicy() {
    private val getUseCase = IsCallerIdDisplayAllowedUseCase()
    private val setUseCase = AllowCallerIdDisplayUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
