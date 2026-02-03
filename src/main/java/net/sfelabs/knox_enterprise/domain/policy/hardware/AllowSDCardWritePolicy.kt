package net.sfelabs.knox_enterprise.domain.policy.hardware

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.AllowSDCardWriteUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.hardware.IsSDCardWriteAllowedUseCase

@PolicyDefinition(
    title = "Block SD Card Write",
    description = "When enabled, prevents writing data to the SD card.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_HARDWARE,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class AllowSDCardWritePolicy : BooleanStatePolicy() {
    private val getUseCase = IsSDCardWriteAllowedUseCase()
    private val setUseCase = AllowSDCardWriteUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
