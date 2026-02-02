package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowKillingActivitiesOnLeaveUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsKillingActivitiesOnLeaveAllowedUseCase

@PolicyDefinition(
    title = "Allow Killing Activities On Leave",
    description = "Allow or disallow the system from killing activities when the user leaves them.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE
    ]
)
class AllowKillingActivitiesOnLeavePolicy : BooleanStatePolicy() {
    private val getUseCase = IsKillingActivitiesOnLeaveAllowedUseCase()
    private val setUseCase = AllowKillingActivitiesOnLeaveUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
