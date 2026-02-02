package net.sfelabs.knox_enterprise.domain.use_cases.browser

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Get browser HTTP proxy setting.
 * @return The HTTP proxy address or empty string if not set
 */
class GetHttpProxyUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, String>() {
    private val browserPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).browserPolicy
    }

    override suspend fun execute(params: Unit): ApiResult<String> {
        return ApiResult.Success(data = browserPolicy.httpProxy ?: "")
    }
}
