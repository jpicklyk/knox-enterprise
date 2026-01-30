package net.sfelabs.knox_enterprise.domain.use_cases.adb

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to turn on or off the Android Debug Bridge.
 * Note: There is no getAdbState equivalent API so it will be necessary to track the state within
 * the app and stave it to a datastore
 */
class SetAdbStateUseCase: SuspendingUseCase<Boolean, Boolean>() {
    private val settingsManager = CustomDeviceManager.getInstance().settingsManager

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (val result = settingsManager.setAdbState(params)) {
            CustomDeviceManager.SUCCESS -> {
                ApiResult.Success(data = params)
            }

            CustomDeviceManager.ERROR_POLICY_RESTRICTED -> {
                ApiResult.Error(DefaultApiError.UnexpectedError("ERROR_POLICY_RESTRICTED: USB debugging has been disabled via API"))
            }

            else -> {
                ApiResult.Error(DefaultApiError.UnexpectedError("An error occurred calling setAdbState: $result"))
            }
        }
    }
}