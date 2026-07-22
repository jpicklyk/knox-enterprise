package net.sfelabs.knox_enterprise.domain.use_cases.audio

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

/**
 * Set all system sounds to silent.
 */
class SetSystemSoundsSilentUseCase : SuspendingUseCase<Unit, Unit>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Unit): ApiResult<Unit> {
        return systemManager.setSystemSoundsSilent()
            .toKnoxApiResult("setSystemSoundsSilent")
    }
}
