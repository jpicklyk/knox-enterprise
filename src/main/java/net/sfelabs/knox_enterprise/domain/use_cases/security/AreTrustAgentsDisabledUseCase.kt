package net.sfelabs.knox_enterprise.domain.use_cases.security

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult

/**
 * Use case to check if trust agents are disabled on the device.
 *
 * STIG V-268927 requires disabling trust agents to ensure the screen-lock policy
 * is properly enforced.
 *
 * Returns true if trust agents are disabled (STIG compliant).
 */
class AreTrustAgentsDisabledUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<ComponentName, Boolean>() {

    private val devicePolicyManager by lazy {
        applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    override suspend fun execute(params: ComponentName): ApiResult<Boolean> {
        val disabledFeatures = devicePolicyManager.getKeyguardDisabledFeatures(params)
        val trustAgentsDisabled = (disabledFeatures and DevicePolicyManager.KEYGUARD_DISABLE_TRUST_AGENTS) != 0
        return ApiResult.Success(data = trustAgentsDisabled)
    }
}
