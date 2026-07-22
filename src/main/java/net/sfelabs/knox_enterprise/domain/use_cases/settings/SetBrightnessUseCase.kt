package net.sfelabs.knox_enterprise.domain.use_cases.settings

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

class SetBrightnessUseCase: SuspendingUseCase<SetBrightnessUseCase.Params, Unit>() {
    data class Params(val enable: Boolean, val level: Int = 255)

    private val settingsManager by lazy { CustomDeviceManager.getInstance().settingsManager }

    suspend operator fun invoke(enable: Boolean, level: Int = 255): ApiResult<Unit> {
        return invoke(Params(enable, level))
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return if (!params.enable) {
            settingsManager.setBrightness(CustomDeviceManager.USE_AUTO)
                .toKnoxApiResult("setBrightness(USE_AUTO)")
        } else {
            settingsManager.setBrightness(params.level).toKnoxApiResult("setBrightness")
        }
    }
}