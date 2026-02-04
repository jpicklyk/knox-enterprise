package net.sfelabs.knox_enterprise.domain.policy.display

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.display.GetDisplayMirroringStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.display.SetDisplayMirroringStateUseCase

@PolicyDefinition(
    title = "Display Mirroring",
    description = "Enable or disable display mirroring (screen casting) on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
    ]
)
class DisplayMirroringPolicy : BooleanStatePolicy() {
    private val getUseCase = GetDisplayMirroringStateUseCase()
    private val setUseCase = SetDisplayMirroringStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
