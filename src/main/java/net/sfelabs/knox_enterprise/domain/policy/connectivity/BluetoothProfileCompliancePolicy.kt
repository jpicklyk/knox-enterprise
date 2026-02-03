package net.sfelabs.knox_enterprise.domain.policy.connectivity

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.model.BluetoothProfile
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.IsBluetoothProfileEnabledUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.connectivity.SetBluetoothProfileStateUseCase

/**
 * Policy to enforce STIG-compliant Bluetooth profile configuration.
 *
 * STIG V-268957 specifies that only these Bluetooth profiles should be allowed:
 * - A2DP (Advanced Audio Distribution Profile)
 * - HEADSET (HSP/HFP - Headset/Hands-Free Profile)
 * - SPP (Serial Port Profile)
 * - AVRCP (Audio/Video Remote Control Profile)
 * - PBAP (Phone Book Access Profile)
 *
 * When enabled, disallowed profiles (HID, PAN, MAP, OPP, SAP) are disabled.
 * When disabled, all profiles are enabled (permissive mode).
 */
@PolicyDefinition(
    title = "Bluetooth Profile STIG Compliance",
    description = "Restrict Bluetooth profiles per STIG V-268957. Disables HID, PAN, MAP, OPP, SAP profiles.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class BluetoothProfileCompliancePolicy : BooleanStatePolicy() {

    private val isProfileEnabledUseCase = IsBluetoothProfileEnabledUseCase()
    private val setProfileStateUseCase = SetBluetoothProfileStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        // Check if all disallowed profiles are disabled
        for (profile in BluetoothProfile.stigDisallowedProfiles) {
            when (val result = isProfileEnabledUseCase(profile)) {
                is ApiResult.Success -> {
                    if (result.data) {
                        // A disallowed profile is enabled, not compliant
                        return ApiResult.Success(false)
                    }
                }
                is ApiResult.Error -> return ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> return ApiResult.NotSupported
            }
        }
        // All disallowed profiles are disabled, compliant
        return ApiResult.Success(true)
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        if (enabled) {
            // Disable all STIG-disallowed profiles
            for (profile in BluetoothProfile.stigDisallowedProfiles) {
                val params = SetBluetoothProfileStateUseCase.Params(
                    profile = profile,
                    enabled = false
                )
                when (val result = setProfileStateUseCase(params)) {
                    is ApiResult.Success -> { /* continue */ }
                    is ApiResult.Error -> return ApiResult.Error(result.apiError, result.exception)
                    ApiResult.NotSupported -> return ApiResult.NotSupported
                }
            }
            // Ensure allowed profiles are enabled
            for (profile in BluetoothProfile.stigAllowedProfiles) {
                val params = SetBluetoothProfileStateUseCase.Params(
                    profile = profile,
                    enabled = true
                )
                when (val result = setProfileStateUseCase(params)) {
                    is ApiResult.Success -> { /* continue */ }
                    is ApiResult.Error -> return ApiResult.Error(result.apiError, result.exception)
                    ApiResult.NotSupported -> return ApiResult.NotSupported
                }
            }
        } else {
            // Enable all profiles (permissive mode)
            for (profile in BluetoothProfile.entries) {
                val params = SetBluetoothProfileStateUseCase.Params(
                    profile = profile,
                    enabled = true
                )
                when (val result = setProfileStateUseCase(params)) {
                    is ApiResult.Success -> { /* continue */ }
                    is ApiResult.Error -> return ApiResult.Error(result.apiError, result.exception)
                    ApiResult.NotSupported -> return ApiResult.NotSupported
                }
            }
        }
        return ApiResult.Success(Unit)
    }
}
