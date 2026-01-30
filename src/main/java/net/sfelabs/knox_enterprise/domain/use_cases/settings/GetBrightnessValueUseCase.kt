package net.sfelabs.knox_enterprise.domain.use_cases.settings

import android.provider.Settings
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class GetBrightnessValueUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Int>() {

    override suspend fun execute(params: Unit): ApiResult<Int> {
        return ApiResult.Success(
            Settings.System.getInt(this@GetBrightnessValueUseCase.applicationContext.contentResolver, Settings.System.SCREEN_BRIGHTNESS)
        )
    }
}