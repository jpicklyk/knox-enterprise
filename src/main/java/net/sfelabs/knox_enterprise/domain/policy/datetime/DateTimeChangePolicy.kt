package net.sfelabs.knox_enterprise.domain.policy.datetime

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.datetime.AllowDateTimeChangeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.datetime.IsDateTimeChangeAllowedUseCase

@PolicyDefinition(
    title = "Date/Time Change",
    description = "Allow or disallow the user to change the device date and time.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.EASILY_REVERSIBLE,
        PolicyCapability.STIG
    ]
)
class DateTimeChangePolicy : BooleanStatePolicy() {
    private val getUseCase = IsDateTimeChangeAllowedUseCase()
    private val setUseCase = AllowDateTimeChangeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
