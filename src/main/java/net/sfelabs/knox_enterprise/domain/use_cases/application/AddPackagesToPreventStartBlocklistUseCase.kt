package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to add packages to the "prevent start" blocklist.
 *
 * Applications on this list are completely prevented from starting/running.
 * This is a stronger restriction than the regular blocklist which prevents
 * installation.
 *
 * STIG V-268932 (CAT I) - Use this to block AI apps that process data in the cloud
 * (e.g., Google Gemini: com.google.android.apps.bard).
 *
 * Returns a list of packages that failed to be added (empty list = all succeeded).
 *
 * @see RemovePackagesFromPreventStartBlocklistUseCase
 */
class AddPackagesToPreventStartBlocklistUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<List<String>, List<String>>() {

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: List<String>): ApiResult<List<String>> {
        val failedPackages = applicationPolicy.addPackagesToPreventStartBlackList(params)
        return ApiResult.Success(data = failedPackages ?: emptyList())
    }
}
