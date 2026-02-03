package net.sfelabs.knox_enterprise.domain.use_cases.application

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to clear all packages from the "prevent start" blocklist.
 *
 * Warning: This removes all packages from the blocklist, allowing all
 * previously blocked apps to start again.
 *
 * @see AddPackagesToPreventStartBlocklistUseCase
 */
class ClearPreventStartBlocklistUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<Unit, Boolean>() {

    private val applicationPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).applicationPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return when (applicationPolicy.clearPreventStartBlackList()) {
            true -> ApiResult.Success(data = true)
            false -> ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to clear prevent start blocklist")
            )
        }
    }
}
