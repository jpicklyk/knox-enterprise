package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.AllowAndroidBeamUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsAndroidBeamAllowedUseCase

@PolicyDefinition(
    title = "Disable Android Beam",
    description = "When enabled, disables Android Beam NFC-based data transfer functionality.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_NETWORK,
        PolicyCapability.AFFECTS_CONNECTIVITY,
    ]
)
class AllowAndroidBeamPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsAndroidBeamAllowedUseCase()
    private val setUseCase = AllowAndroidBeamUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
