package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.GetAllowUserWifiPolicyChangesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetAllowUserWifiPolicyChangesUseCase

@PolicyDefinition(
    title = "Allow User WiFi Policy Changes",
    description = "Allow or disallow the user from changing enterprise WiFi policy settings.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_WIFI,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowUserWifiPolicyChangesPolicy : BooleanStatePolicy() {
    private val getUseCase = GetAllowUserWifiPolicyChangesUseCase()
    private val setUseCase = SetAllowUserWifiPolicyChangesUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
