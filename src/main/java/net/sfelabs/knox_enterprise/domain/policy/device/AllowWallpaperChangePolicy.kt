package net.sfelabs.knox_enterprise.domain.policy.device

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.map
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.device.AllowWallpaperChangeUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.device.IsWallpaperChangeAllowedUseCase

@PolicyDefinition(
    title = "Block Wallpaper Change",
    description = "When enabled, prevents users from changing the device wallpaper.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_DISPLAY,
    ]
)
class AllowWallpaperChangePolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = IsWallpaperChangeAllowedUseCase()
    private val setUseCase = AllowWallpaperChangeUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled).map { }
}
