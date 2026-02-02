package net.sfelabs.knox_enterprise.domain.policy.telephony

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.IsBlockSmsWithStorageEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.telephony.SetBlockSmsWithStorageUseCase

@PolicyDefinition(
    title = "Block SMS With Storage",
    description = "Enable or disable blocking SMS messages from being stored on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_CALLING,
        PolicyCapability.REQUIRES_SIM
    ]
)
class BlockSmsWithStorageEnabledPolicy : BooleanStatePolicy() {
    private val getUseCase = IsBlockSmsWithStorageEnabledUseCase()
    private val setUseCase = SetBlockSmsWithStorageUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
