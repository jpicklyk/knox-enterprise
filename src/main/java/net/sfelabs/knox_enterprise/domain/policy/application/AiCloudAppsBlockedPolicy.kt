package net.sfelabs.knox_enterprise.domain.policy.application

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.model.StigBlockedApps
import net.sfelabs.knox_enterprise.domain.use_cases.application.AddPackagesToPreventStartBlocklistUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.GetAllBlockedPackageNamesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.RemovePackagesFromPreventStartBlocklistUseCase

/**
 * Policy to block AI applications that process data in the cloud.
 *
 * STIG V-268932 (CAT I - Critical) requires excluding AI apps that process
 * data in the cloud (e.g., Google Gemini, Samsung Bixby, ChatGPT).
 *
 * When enabled, all known AI cloud processing apps from [StigBlockedApps.AI_CLOUD_PROCESSING_APPS]
 * are added to the prevent-start blocklist, completely blocking them from running.
 *
 * When disabled, these apps are removed from the blocklist and can run normally.
 *
 * Note: Additional apps can be blocked using [AddPackagesToPreventStartBlocklistUseCase] directly.
 */
@PolicyDefinition(
    title = "Block AI Cloud Processing Apps",
    description = "Block AI applications that process data in the cloud (Google Gemini, Bixby, ChatGPT, etc.) per STIG V-268932.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class AiCloudAppsBlockedPolicy : BooleanStatePolicy() {
    private val getBlockedNamesUseCase = GetAllBlockedPackageNamesUseCase()
    private val addToBlocklistUseCase = AddPackagesToPreventStartBlocklistUseCase()
    private val removeFromBlocklistUseCase = RemovePackagesFromPreventStartBlocklistUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getBlockedNamesUseCase()) {
            is ApiResult.Success -> {
                val allBlocked = StigBlockedApps.AI_CLOUD_PROCESSING_APPS.all { it in result.data }
                ApiResult.Success(allBlocked)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        return if (enabled) {
            when (val result = addToBlocklistUseCase(StigBlockedApps.AI_CLOUD_PROCESSING_APPS)) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> ApiResult.NotSupported
            }
        } else {
            when (val result = removeFromBlocklistUseCase(StigBlockedApps.AI_CLOUD_PROCESSING_APPS)) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> ApiResult.NotSupported
            }
        }
    }
}
