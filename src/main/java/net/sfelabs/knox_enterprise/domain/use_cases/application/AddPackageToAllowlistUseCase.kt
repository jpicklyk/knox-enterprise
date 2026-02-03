package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to add a package to the application allowlist (whitelist).
 *
 * When combined with setting installation mode to DISALLOW, only allowlisted
 * applications can be installed on the device.
 *
 * STIG V-268929 requires enforcing app installation from authorized repos only.
 * STIG V-268930 requires application allowlist by name for work environment.
 *
 * @see RemovePackageFromAllowlistUseCase
 * @see GetAllowedPackagesUseCase
 * @see SetApplicationInstallationModeUseCase
 */
class AddPackageToAllowlistUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<String, Boolean>() {

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return when (applicationPolicy.addAppPackageNameToWhiteList(params)) {
            true -> ApiResult.Success(data = true)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to add package '$params' to allowlist")
            )
        }
    }
}
