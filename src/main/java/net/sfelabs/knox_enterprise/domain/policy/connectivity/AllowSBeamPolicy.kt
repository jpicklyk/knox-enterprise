package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowSBeamUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsSBeamAllowedUseCase

@PolicyDefinition(
    title = "Disable S Beam",
    description = "When enabled, disables Samsung S Beam NFC-based data transfer functionality.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.AFFECTS_CONNECTIVITY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class AllowSBeamPolicy : BooleanStatePolicy() {
    private val getUseCase = IsSBeamAllowedUseCase()
    private val setUseCase = AllowSBeamUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
