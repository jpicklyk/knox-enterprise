package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsBlockMmsWithStorageEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.SetBlockMmsWithStorageUseCase

@PolicyDefinition(
    title = "Block MMS With Storage",
    description = "Enable or disable blocking MMS messages from being stored on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class BlockMmsWithStorageEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsBlockMmsWithStorageEnabledUseCase()
    private val setUseCase = SetBlockMmsWithStorageUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
