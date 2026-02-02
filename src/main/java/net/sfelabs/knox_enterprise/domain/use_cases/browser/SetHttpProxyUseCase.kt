package net.sfelabs.knox_enterprise.domain.use_cases.browser

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Set browser HTTP proxy.
 * @param params The HTTP proxy address (e.g., "proxy.example.com:8080")
 */
class SetHttpProxyUseCase : WithAndroidApplicationContext, SuspendingUseCase<String, Boolean>() {
    private val browserPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).browserPolicy
    }

    override suspend fun execute(params: String): ApiResult<Boolean> {
        return ApiResult.Success(data = browserPolicy.setHttpProxy(params))
    }
}
