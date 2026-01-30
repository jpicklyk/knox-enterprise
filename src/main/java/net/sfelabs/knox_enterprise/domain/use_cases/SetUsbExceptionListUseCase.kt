package net.sfelabs.knox_enterprise.domain.use_cases

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetUsbExceptionListUseCase: WithAndroidApplicationContext, SuspendingUseCase<SetUsbExceptionListUseCase.Params, Unit>() {
    class Params(val usbClassList: Int)

    private val restrictionPolicy =
        EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy

    suspend operator fun invoke(usbClassList: Int): ApiResult<Unit> {
        return invoke(Params(usbClassList))
    }

    override suspend fun execute(params: Params): ApiResult<Unit> {
        return if (restrictionPolicy.setUsbExceptionList(params.usbClassList))
            ApiResult.Success(Unit)
        else
            ApiResult.Error(
                DefaultApiError.UnexpectedError(
                    "The API setUsbExceptionList($params.usbClassList) failed"
                )
            )
    }
}