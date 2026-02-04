package net.sfelabs.knox_enterprise.domain.policy.display

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.display.GetLcdBacklightStateUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.display.SetLcdBacklightStateUseCase

@PolicyDefinition(
    title = "Disable LCD Backlight",
    description = "When enabled, disables the LCD backlight on the device.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
    ]
)
class LcdBacklightPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetLcdBacklightStateUseCase()
    private val setUseCase = SetLcdBacklightStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
