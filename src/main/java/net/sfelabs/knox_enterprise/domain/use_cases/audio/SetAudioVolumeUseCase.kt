package net.sfelabs.knox_enterprise.domain.use_cases.audio

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Set audio volume for a specific stream type.
 * Stream types: STREAM_VOICE_CALL=0, STREAM_SYSTEM=1, STREAM_RING=2, STREAM_MUSIC=3, STREAM_ALARM=4, STREAM_NOTIFICATION=5
 */
class SetAudioVolumeUseCase : SuspendingUseCase<SetAudioVolumeUseCase.Params, Unit>() {
    data class Params(val streamType: Int, val volume: Int)

    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return when (val result = systemManager.setAudioVolume(params.streamType, params.volume)) {
            CustomDeviceManager.SUCCESS -> ApiResult.Success(Unit)
            else -> ApiResult.Error(DefaultApiError.UnexpectedError("Failed to set audio volume: error code $result"))
        }
    }
}
