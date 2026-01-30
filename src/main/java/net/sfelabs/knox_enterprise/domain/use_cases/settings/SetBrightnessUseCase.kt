package net.sfelabs.knox_enterprise.domain.use_cases.settings

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetBrightnessUseCase: SuspendingUseCase<SetBrightnessUseCase.Params, Unit>() {
    data class Params(val enable: Boolean, val level: Int = 255)

    private val settingsManager = CustomDeviceManager.getInstance().settingsManager

    suspend operator fun invoke(enable: Boolean, level: Int = 255): ApiResult<Unit> {
        return invoke(Params(enable, level))
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return if (!params.enable) {
            settingsManager.setBrightness(CustomDeviceManager.USE_AUTO)
            ApiResult.Success(Unit)
        } else {
            val result = settingsManager.setBrightness(params.level)
            if (result == CustomDeviceManager.SUCCESS) {
                ApiResult.Success(Unit)
            } else {
                ApiResult.Error(DefaultApiError.UnexpectedError(
                    "An invalid brightness level of '${params.level}' was passed"
                ))
            }
        }
    }
}