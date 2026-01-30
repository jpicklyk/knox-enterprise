package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.custom.CustomDeviceManager
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetMobileDataRoamingStateUseCase: SuspendingUseCase<Boolean, Unit>() {
    private val settingsManager = CustomDeviceManager.getInstance().settingsManager

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        val result = settingsManager.setMobileDataRoamingState(params)
        return if(result == CustomDeviceManager.SUCCESS)
            ApiResult.Success(Unit)
        else
            ApiResult.Error(
                DefaultApiError.UnexpectedError(
                "The API setMobileDataRoamingState($params) failed: $result"
                )
            )
    }
}