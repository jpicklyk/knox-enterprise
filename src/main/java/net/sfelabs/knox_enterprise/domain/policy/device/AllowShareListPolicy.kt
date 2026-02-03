package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowShareListUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsShareListAllowedUseCase

@PolicyDefinition(
    title = "Block Share List",
    description = "When enabled, hides the Share Via list when sharing content.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class AllowShareListPolicy : BooleanStatePolicy() {
    private val getUseCase = IsShareListAllowedUseCase()
    private val setUseCase = AllowShareListUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
