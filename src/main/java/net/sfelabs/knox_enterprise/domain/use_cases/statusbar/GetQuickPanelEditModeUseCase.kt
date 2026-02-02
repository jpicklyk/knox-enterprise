package net.sfelabs.knox_enterprise.domain.use_cases.statusbar

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get quick panel edit mode.
 * @return Quick panel edit mode value
 */
class GetQuickPanelEditModeUseCase : SuspendingUseCase<Unit, Int>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(data = systemManager.quickPanelEditMode)
    }
}
