package net.sfelabs.knox_enterprise.domain.policy.system

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.system.GetInfraredStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.system.SetInfraredStateUseCase

@PolicyDefinition(
    title = "Infrared",
    description = "Enable or disable the infrared (IR) blaster.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.EASILY_REVERSIBLE
    ]
)
class InfraredPolicy : BooleanStatePolicy() {
    private val getUseCase = GetInfraredStateUseCase()
    private val setUseCase = SetInfraredStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
