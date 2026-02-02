package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsDataCallLimitEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.SetDataCallLimitEnabledUseCase

@PolicyDefinition(
    title = "Data Call Limit Enabled",
    description = "Enable or disable data call limiting on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_RADIO,
        PolicyCapability.REQUIRES_SIM
    ]
)
class DataCallLimitEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsDataCallLimitEnabledUseCase()
    private val setUseCase = SetDataCallLimitEnabledUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
