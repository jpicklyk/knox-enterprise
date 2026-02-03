package net.sfelabs.knox_enterprise.domain.use_cases.security

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import net.sfelabs.knox.core.android.WithAndroidApplicationContext
import net.sfelabs.knox.core.domain.usecase.base.SuspendingUseCase
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.domain.usecase.model.DefaultApiError

/**
 * Use case to disable or enable trust agents on the device.
 *
 * STIG V-268927 requires disabling trust agents to ensure the screen-lock policy
 * is properly enforced without bypass mechanisms like Smart Lock.
 *
 * Trust agents include features like:
 * - Smart Lock (trusted places, trusted devices, on-body detection)
 * - Face unlock (when used as trust agent)
 * - Voice Match unlock
 *
 * When disabled = true, trust agents are blocked from bypassing the lock screen.
 */
class SetTrustAgentsDisabledUseCase : WithAndroidApplicationContext,
    SuspendingUseCase<SetTrustAgentsDisabledUseCase.Params, Boolean>() {

    data class Params(
        val adminComponent: ComponentName,
        val disabled: Boolean
    )

    private val devicePolicyManager by lazy {
        applicationContext.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    override suspend fun execute(params: Params): ApiResult<Boolean> {
        return try {
            // Get current disabled features
            val currentFeatures = devicePolicyManager.getKeyguardDisabledFeatures(params.adminComponent)

            val newFeatures = if (params.disabled) {
                // Add KEYGUARD_DISABLE_TRUST_AGENTS flag
                currentFeatures or DevicePolicyManager.KEYGUARD_DISABLE_TRUST_AGENTS
            } else {
                // Remove KEYGUARD_DISABLE_TRUST_AGENTS flag
                currentFeatures and DevicePolicyManager.KEYGUARD_DISABLE_TRUST_AGENTS.inv()
            }

            devicePolicyManager.setKeyguardDisabledFeatures(params.adminComponent, newFeatures)
            ApiResult.Success(data = params.disabled)
        } catch (e: SecurityException) {
            ApiResult.Error(
                DefaultApiError.UnexpectedError("Failed to set trust agents disabled: ${e.message}")
            )
        }
    }
}
