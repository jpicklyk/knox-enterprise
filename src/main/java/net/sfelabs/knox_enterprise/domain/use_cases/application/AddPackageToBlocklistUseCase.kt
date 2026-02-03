package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to add a package to the application blocklist (blacklist).
 *
 * When a package is added to the blocklist, the application cannot be installed
 * or run on the device.
 *
 * STIG V-268930 requires application allowlist/blocklist by name.
 * STIG V-268932 (CAT I) can use this to block AI apps like Google Gemini.
 *
 * @see RemovePackageFromBlocklistUseCase
 * @see GetBlockedPackagesUseCase
 */
class AddPackageToBlocklistUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<String, Boolean>() {

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return when (applicationPolicy.addAppPackageNameToBlackList(params)) {
            true -> ApiResult.Success(data = true)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to add package '$params' to blocklist")
            )
        }
    }
}
