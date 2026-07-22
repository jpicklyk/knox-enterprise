package net.sfelabs.knox_enterprise.domain.use_cases.audit

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

class IsAuditLogEnabledUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val auditLog by lazy {
        EnterpriseKnoxManager.getInstance(applicationContext).auditLogPolicy
    }


    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return ApiResult.Success(auditLog.isAuditLogEnabled)
    }
}