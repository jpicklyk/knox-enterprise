package net.sfelabs.knox_enterprise.domain.use_cases.firewall

import com.samsung.android.knox.EnterpriseDeviceManager
import com.samsung.android.knox.net.firewall.FirewallResponse
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Enable or disable domain filter reporting.
 */
class EnableDomainFilterReportUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Unit>() {
    private val firewall by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).firewall
    }

    override suspend fun execute(params: Boolean): ApiResult<Unit> {
        val response = firewall.enableDomainFilterReport(params)
        return if (response.result == FirewallResponse.Result.SUCCESS) {
            ApiResult.Success(Unit)
        } else {
            ApiResult.Error(DefaultApiError.UnexpectedError("Failed to ${if (params) "enable" else "disable"} domain filter report: ${response.errorCode}"))
        }
    }
}
