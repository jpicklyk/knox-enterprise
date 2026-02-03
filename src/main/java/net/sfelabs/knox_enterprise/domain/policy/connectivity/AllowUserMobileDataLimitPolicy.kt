package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowUserMobileDataLimitUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsUserMobileDataLimitAllowedUseCase

@PolicyDefinition(
    title = "Block User Mobile Data Limit",
    description = "When enabled, prevents users from setting mobile data usage limits.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_RADIO,
        PolicyCapability.REQUIRES_SIM
    ]
)
class AllowUserMobileDataLimitPolicy : BooleanStatePolicy() {
    private val getUseCase = IsUserMobileDataLimitAllowedUseCase()
    private val setUseCase = AllowUserMobileDataLimitUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
