package net.sfelabs.knox_enterprise.domain.use_cases.audio

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox_enterprise.domain.toKnoxApiResult

/**
 * Set system ringtone.
 */
class SetSystemRingtoneUseCase : SuspendingUseCase<SetSystemRingtoneUseCase.Params, Unit>() {
    data class Params(val ringtoneType: Int, val ringtonePath: String)

    private val systemManager by lazy {
        CustomDeviceManager.getInstance().systemManager
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return systemManager.setSystemRingtone(params.ringtoneType, params.ringtonePath)
            .toKnoxApiResult("setSystemRingtone")
    }
}
