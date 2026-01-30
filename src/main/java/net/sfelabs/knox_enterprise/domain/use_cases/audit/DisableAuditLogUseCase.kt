package net.sfelabs.knox_enterprise.domain.use_cases.audit

import com.samsung.android.knox.EnterpriseKnoxManager
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

class DisableAuditLogUseCase : WithAndroidApplicationContext, SuspendingUseCase<Unit, Boolean>() {
    private val enterpriseKnoxManager = EnterpriseKnoxManager.getInstance(applicationContext)
    private val auditLog = enterpriseKnoxManager.auditLogPolicy


    override suspend fun execute(params: Unit): ApiResult<Boolean> {
        return when (auditLog.disableAuditLog()) {
            true -> {
                ApiResult.Success(data = true)
            }
            false -> {
                ApiResult.Error(
                    DefaultApiError.UnexpectedError(
                        "Failure occurred applying enableAuditLog()"
                    )
                )
            }
        }
    }


}