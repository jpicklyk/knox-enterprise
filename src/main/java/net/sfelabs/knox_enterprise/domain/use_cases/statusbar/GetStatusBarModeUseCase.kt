package net.sfelabs.knox_enterprise.domain.use_cases.statusbar

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get status bar mode.
 * @return Status bar mode value
 */
class GetStatusBarModeUseCase : SuspendingUseCase<Unit, Int>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(data = systemManager.statusBarMode)
    }
}
