package net.sfelabs.knox_enterprise.domain.use_cases.password

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to get the minimum password length requirement.
 *
 * STIG V-268924 requires a minimum password length of 6 characters.
 */
class GetMinPasswordLengthUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<ComponentName, Int>() {

    private val devicePolicyManager by lazy {
        applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    override suspend fun execute(params: ComponentName): ApiResult<Int> {
        return ApiResult.Success(data = devicePolicyManager.getPasswordMinimumLength(params))
    }
}
