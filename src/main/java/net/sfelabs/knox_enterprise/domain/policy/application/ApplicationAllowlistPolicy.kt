package net.sfelabs.knox_enterprise.domain.policy.application

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.application.ClearApplicationAllowlistUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.GetAllAllowedPackageNamesUseCase

/**
 * Policy to manage the application allowlist.
 *
 * When the allowlist is active (contains packages), only those packages
 * are permitted to run on the device. All other applications are blocked.
 *
 * - [getEnabled] returns true if any packages are in the allowlist
 * - [setEnabled] with false clears the allowlist (allows all apps)
 * - [setEnabled] with true is a no-op; use [AddPackageToAllowlistUseCase] to add packages
 *
 * For adding specific packages to the allowlist, use the underlying use cases:
 * - [AddPackageToAllowlistUseCase] - add a package
 * - [RemovePackageFromAllowlistUseCase] - remove a package
 * - [GetAllowedPackagesUseCase] - get all allowed packages with admin info
 * - [GetAllAllowedPackageNamesUseCase] - get flat set of allowed package names
 */
@PolicyDefinition(
    title = "Application Allowlist",
    description = "Control application allowlist. When active, only allowlisted apps can run.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class ApplicationAllowlistPolicy : BooleanStatePolicy() {
    private val getAllowedNamesUseCase = GetAllAllowedPackageNamesUseCase()
    private val clearAllowlistUseCase = ClearApplicationAllowlistUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        return when (val result = getAllowedNamesUseCase()) {
            is ApiResult.Success -> ApiResult.Success(result.data.isNotEmpty())
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        return if (enabled) {
            // Enabling the allowlist requires adding specific packages.
            // This is a no-op; use AddPackageToAllowlistUseCase instead.
            ApiResult.Success(Unit)
        } else {
            when (val result = clearAllowlistUseCase()) {
                is ApiResult.Success -> ApiResult.Success(Unit)
                is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                ApiResult.NotSupported -> ApiResult.NotSupported
            }
        }
    }
}
