package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowNfcStateChangeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsNfcStateChangeAllowedUseCase

@PolicyDefinition(
    title = "Allow NFC State Change",
    description = "Allow or disallow the user from changing the NFC on/off state.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowNfcStateChangePolicy : BooleanStatePolicy() {
    private val getUseCase = IsNfcStateChangeAllowedUseCase()
    private val setUseCase = AllowNfcStateChangeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
