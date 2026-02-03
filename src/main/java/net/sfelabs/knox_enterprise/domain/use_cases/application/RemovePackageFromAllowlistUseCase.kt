package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to remove a package from the application allowlist (whitelist).
 *
 * @see AddPackageToAllowlistUseCase
 * @see GetAllowedPackagesUseCase
 */
class RemovePackageFromAllowlistUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<String, Boolean>() {

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return when (applicationPolicy.removeAppPackageNameFromWhiteList(params)) {
            true -> ApiResult.Success(data = true)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to remove package '$params' from allowlist")
            )
        }
    }
}
