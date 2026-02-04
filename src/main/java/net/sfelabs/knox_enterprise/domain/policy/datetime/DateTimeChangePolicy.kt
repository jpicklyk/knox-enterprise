package net.sfelabs.knox_enterprise.domain.policy.datetime

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.datetime.AllowDateTimeChangeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.datetime.IsDateTimeChangeAllowedUseCase

@PolicyDefinition(
    title = "Block Date/Time Change",
    description = "When enabled, prevents users from changing the device date and time.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.STIG
    ]
)
class DateTimeChangePolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsDateTimeChangeAllowedUseCase()
    private val setUseCase = AllowDateTimeChangeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
