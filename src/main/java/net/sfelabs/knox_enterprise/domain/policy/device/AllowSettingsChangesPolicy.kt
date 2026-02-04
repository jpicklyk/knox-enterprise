package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowSettingsChangesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsSettingsChangesAllowedUseCase

@PolicyDefinition(
    title = "Block Settings Changes",
    description = "When enabled, denies user access to the Settings application.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
    ]
)
class AllowSettingsChangesPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsSettingsChangesAllowedUseCase()
    private val setUseCase = AllowSettingsChangesUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
