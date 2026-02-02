package net.sfelabs.knox_enterprise.domain.use_cases.device

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class AllowWallpaperChangeUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val restrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return when (restrictionPolicy.allowWallpaperChange(params)) {
            true -> ApiResult.Success(data = params)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set wallpaper change allowance to $params")
            )
        }
    }
}
