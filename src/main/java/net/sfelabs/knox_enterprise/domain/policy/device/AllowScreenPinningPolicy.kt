package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowScreenPinningUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsScreenPinningAllowedUseCase

@PolicyDefinition(
    title = "Allow Screen Pinning",
    description = "Allow or disallow the user from using the screen pinning feature.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class AllowScreenPinningPolicy : BooleanStatePolicy() {
    private val getUseCase = IsScreenPinningAllowedUseCase()
    private val setUseCase = AllowScreenPinningUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
