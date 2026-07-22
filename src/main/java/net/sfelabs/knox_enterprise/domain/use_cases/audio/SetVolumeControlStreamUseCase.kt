package net.sfelabs.knox_enterprise.domain.use_cases.audio

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

/**
 * Set the volume control stream type.
 * @param params Stream type: STREAM_VOICE_CALL=0, STREAM_SYSTEM=1, STREAM_RING=2, STREAM_MUSIC=3, STREAM_ALARM=4, STREAM_NOTIFICATION=5
 */
class SetVolumeControlStreamUseCase : SuspendingUseCase<Int, Unit>() {
    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Int): ApiResult<Unit> {
        return systemManager.setVolumeControlStream(params)
            .toKnoxApiResult("setVolumeControlStream")
    }
}
