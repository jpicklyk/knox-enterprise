package net.sfelabs.knox_enterprise.domain.use_cases.password

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class SetMaxCharacterOccurrencesUseCase : WithAndroidApplicationContext, SuspendingUseCase<Int, Boolean>() {
    private val passwordPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).passwordPolicy
    }

    override suspend fun execute(params: Int): ApiResult<Boolean> {
        return when (passwordPolicy.setMaximumCharacterOccurrences(params)) {
            true -> ApiResult.Success(data = true)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set maximum character occurrences to $params")
            )
        }
    }
}
