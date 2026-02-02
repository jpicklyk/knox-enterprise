package net.sfelabs.knox_enterprise.domain.policy.hardware

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.AllowSDCardMoveUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.IsSDCardMoveAllowedUseCase

@PolicyDefinition(
    title = "Allow SD Card Move",
    description = "Allow or disallow moving applications and data to the SD card.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE
    ]
)
class AllowSDCardMovePolicy : BooleanStatePolicy() {
    private val getUseCase = IsSDCardMoveAllowedUseCase()
    private val setUseCase = AllowSDCardMoveUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
