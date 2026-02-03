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
 * Policy to block payment applications.
 *
 * STIG V-268931 requires restricting payment apps as part of cloud backup,
 * diagnostic data, voice assistants, and payment app restrictions.
 *
 * When enabled, known payment apps from [StigBlockedApps.PAYMENT_APPS]
 * are added to the prevent-start blocklist.
 *
 * Note: Additional apps can be blocked using [AddPackagesToPreventStartBlocklistUseCase] directly.
 */
@PolicyDefinition(
    title = "Block Payment Apps",
    description = "Block payment applications (Google Pay, Samsung Pay) per STIG V-268931.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class PaymentAppsBlockedPolicy : BooleanStatePolicy() {
    private val getBlockedNamesUseCase = GetAllBlockedPackageNamesUseCase()
    private val addToBlocklistUseCase = AddPackagesToPreventStartBlocklistUseCase()
    private val removeFromBlocklistUseCase = RemovePackagesFromPreventStartBlocklistUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getBlockedNamesUseCase()) {
            is ApiResult.Success -> {
                val allBlocked = StigBlockedApps.PAYMENT_APPS.all { it in result.data }
                ApiResult.Success(allBlocked)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        return if (enabled) {
            when (val result = addToBlocklistUseCase(StigBlockedApps.PAYMENT_APPS)) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> ApiResult.NotSupported
            }
        } else {
            when (val result = removeFromBlocklistUseCase(StigBlockedApps.PAYMENT_APPS)) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> ApiResult.NotSupported
            }
        }
    }
}
