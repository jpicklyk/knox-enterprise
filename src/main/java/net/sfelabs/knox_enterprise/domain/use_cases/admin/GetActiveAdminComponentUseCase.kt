package net.sfelabs.knox_enterprise.domain.use_cases.admin

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to get the active admin component for this application.
 *
 * This finds the first active device admin that belongs to this application's package.
 * Returns an error if no admin is active for this package.
 */
class GetActiveAdminComponentUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<Unit, ComponentName>() {

    private val devicePolicyManager by lazy {
        applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    override suspend fun execute(params: Unit): ApiResult<ComponentName> {
        val packageName = applicationContext.packageName
        val activeAdmins = devicePolicyManager.activeAdmins
            ?: return ApiResult.Error(DefaultApiError.UnexpectedError("No active device admins"))

        val ourAdmin = activeAdmins.find { it.packageName == packageName }
            ?: return ApiResult.Error(DefaultApiError.UnexpectedError("No active admin for this application"))

        return ApiResult.Success(ourAdmin)
    }
}
