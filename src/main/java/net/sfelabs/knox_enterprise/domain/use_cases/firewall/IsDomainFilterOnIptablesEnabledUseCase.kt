package net.sfelabs.knox_enterprise.domain.use_cases.firewall

import com.samsung.android.knox.EnterpriseDeviceManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Check if domain filtering on iptables is enabled.
 */
class IsDomainFilterOnIptablesEnabledUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val firewall by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).firewall
    }

    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(data = firewall.isDomainFilterOnIptablesEnabled)
    }
}
