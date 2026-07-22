package net.sfelabs.knox_enterprise.domain.use_cases.statusbar

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

/**
 * Enable or disable screen off on status bar double tap.
 */
class SetScreenOffOnStatusBarDoubleTapStateUseCase : SuspendingUseCase<Boolean, Unit>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        return systemManager.setScreenOffOnStatusBarDoubleTapState(params)
            .toKnoxApiResult("setScreenOffOnStatusBarDoubleTapState")
    }
}
