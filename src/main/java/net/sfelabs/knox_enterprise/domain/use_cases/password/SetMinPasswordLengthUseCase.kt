package net.sfelabs.knox_enterprise.domain.use_cases.password

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to set the minimum password length requirement.
 *
 * STIG V-268924 requires a minimum password length of 6 characters.
 *
 * Note: This uses Android's DevicePolicyManager as Knox SDK's PasswordPolicy
 * doesn't expose a direct setMinPasswordLength method.
 */
class SetMinPasswordLengthUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<SetMinPasswordLengthUseCase.Params, Boolean>() {

    data class Params(
        val adminComponent: ComponentName,
        val minLength: Int
    )

    private val devicePolicyManager by lazy {
        applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return try {
            devicePolicyManager.setPasswordMinimumLength(params.adminComponent, params.minLength)
            ApiResult.Success(data = true)
        } catch (e: SecurityException) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set minimum password length: ${e.message}")
            )
        }
    }
}
