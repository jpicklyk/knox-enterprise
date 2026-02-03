package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import com.samsung.android.knox.application.AppControlInfo
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to get all packages in the allowlist (whitelist).
 *
 * Returns a list of [AppControlInfo] containing package names and their
 * allowlist status across all administrators.
 *
 * @see AddPackageToAllowlistUseCase
 * @see RemovePackageFromAllowlistUseCase
 */
class GetAllowedPackagesUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<Unit, List<AppControlInfo>>() {

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<List<AppControlInfo>> {
        val allowedPackages = applicationPolicy.appPackageNamesAllWhiteLists
        return ApiResult.Success(data = allowedPackages ?: emptyList())
    }
}
