package net.sfelabs.knox_enterprise.domain.policy.security

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.security.AllowLocalContactStorageUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.security.IsLocalContactStorageAllowedUseCase

@PolicyDefinition(
    title = "Block Local Contact Storage",
    description = "When enabled, prevents storing contacts locally on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowLocalContactStoragePolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsLocalContactStorageAllowedUseCase()
    private val setUseCase = AllowLocalContactStorageUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
