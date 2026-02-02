package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.GetEmergencyCallOnlyUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.SetEmergencyCallOnlyUseCase

@PolicyDefinition(
    title = "Emergency Call Only",
    description = "Restrict device to only allow emergency calls. When enabled, users can only make emergency calls.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class EmergencyCallOnlyPolicy : BooleanStatePolicy() {
    private val getUseCase = GetEmergencyCallOnlyUseCase()
    private val setUseCase = SetEmergencyCallOnlyUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
