package net.sfelabs.knox_enterprise.domain.use_cases.statusbar

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Set quick panel buttons configuration.
 * @param params Quick panel buttons bitmask
 */
class SetQuickPanelButtonsUseCase : SuspendingUseCase<Int, Unit>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Int): ApiResult<Unit> {
        return when (val result = systemManager.setQuickPanelButtons(params)) {
            CustomDeviceManager.SUCCESS -> ApiResult.Success(Unit)
            else -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to set quick panel buttons: error code $result"))
        }
    }
}
