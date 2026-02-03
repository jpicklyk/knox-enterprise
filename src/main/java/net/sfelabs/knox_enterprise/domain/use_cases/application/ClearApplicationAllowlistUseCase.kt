package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to clear the entire application allowlist (whitelist).
 *
 * This removes all packages from the allowlist, effectively disabling
 * allowlist-based app restrictions (all apps will be allowed to run).
 *
 * @see AddPackageToAllowlistUseCase
 * @see RemovePackageFromAllowlistUseCase
 * @see GetAllowedPackagesUseCase
 */
class ClearApplicationAllowlistUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<Unit, Boolean>() {

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return when (applicationPolicy.clearAppPackageNameFromList()) {
            true -> ApiResult.Success(data = true)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to clear application allowlist")
            )
        }
    }
}
