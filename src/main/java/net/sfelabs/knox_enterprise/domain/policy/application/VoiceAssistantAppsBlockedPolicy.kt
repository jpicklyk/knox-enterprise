package net.sfelabs.knox_enterprise.domain.policy.application

import com.samsung.android.knox.application.AppControlInfo
import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.model.StigBlockedApps
import net.sfelabs.knox_enterprise.domain.use_cases.application.AddPackagesToPreventStartBlocklistUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.GetBlockedPackagesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.RemovePackagesFromPreventStartBlocklistUseCase

/**
 * Policy to block voice assistant applications.
 *
 * STIG V-268931 requires restricting voice assistants as part of cloud backup,
 * diagnostic data, and voice assistant restrictions.
 *
 * When enabled, known voice assistant apps from [StigBlockedApps.VOICE_ASSISTANT_APPS]
 * are added to the prevent-start blocklist.
 *
 * Note: Additional apps can be blocked using [AddPackagesToPreventStartBlocklistUseCase] directly.
 */
@PolicyDefinition(
    title = "Block Voice Assistant Apps",
    description = "Block voice assistant applications (Google Assistant, Bixby, Alexa) per STIG V-268931.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class VoiceAssistantAppsBlockedPolicy : BooleanStatePolicy() {
    private val getBlockedPackagesUseCase = GetBlockedPackagesUseCase()
    private val addToBlocklistUseCase = AddPackagesToPreventStartBlocklistUseCase()
    private val removeFromBlocklistUseCase = RemovePackagesFromPreventStartBlocklistUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getBlockedPackagesUseCase()) {
            is ApiResult.Success -> {
                val blockedPackages: List<AppControlInfo> = result.data
                val blockedNames = mutableSetOf<String>()
                for (info in blockedPackages) {
                    blockedNames.addAll(info.entries)
                }
                val allBlocked = StigBlockedApps.VOICE_ASSISTANT_APPS.all { it in blockedNames }
                ApiResult.Success(allBlocked)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        return if (enabled) {
            when (val result = addToBlocklistUseCase(StigBlockedApps.VOICE_ASSISTANT_APPS)) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> ApiResult.NotSupported
            }
        } else {
            when (val result = removeFromBlocklistUseCase(StigBlockedApps.VOICE_ASSISTANT_APPS)) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> ApiResult.NotSupported
            }
        }
    }
}
