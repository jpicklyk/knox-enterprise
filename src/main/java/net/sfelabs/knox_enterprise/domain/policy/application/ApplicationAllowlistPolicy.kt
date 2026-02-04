package net.sfelabs.knox_enterprise.domain.policy.application

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.ConfigurableStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyParameters
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.application.AddPackageToAllowlistUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.ClearApplicationAllowlistUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.GetAllAllowedPackageNamesUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.application.RemovePackageFromAllowlistUseCase

/**
 * Policy to manage the application allowlist.
 *
 * When the allowlist is active (contains packages), only those packages
 * are permitted to run on the device. All other applications are blocked.
 *
 * This is a configurable policy that allows:
 * - Viewing the current list of allowed packages
 * - Adding packages to the allowlist (when enabling with a package name)
 * - Clearing the entire allowlist (when disabling)
 * - Removing individual packages from the allowlist
 *
 * @see AddPackageToAllowlistUseCase
 * @see RemovePackageFromAllowlistUseCase
 * @see ClearApplicationAllowlistUseCase
 * @see GetAllAllowedPackageNamesUseCase
 */
@PolicyDefinition(
    title = "Application Allowlist",
    description = "Control which applications can run on the device. When active, only allowlisted apps are permitted.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE
    ]
)
class ApplicationAllowlistPolicy : ConfigurableStatePolicy<
    ApplicationAllowlistState,
    AllowlistApiData,
    ApplicationAllowlistConfiguration
>(stateMapping = StateMapping.DIRECT) {

    private val getAllowedNamesUseCase = GetAllAllowedPackageNamesUseCase()
    private val addPackageUseCase = AddPackageToAllowlistUseCase()
    private val removePackageUseCase = RemovePackageFromAllowlistUseCase()
    private val clearAllowlistUseCase = ClearApplicationAllowlistUseCase()

    override val configuration = ApplicationAllowlistConfiguration(stateMapping = stateMapping)

    override val defaultValue = ApplicationAllowlistState(
        isEnabled = false,
        allowedPackages = emptySet(),
        packageToAdd = ""
    )

    override suspend fun getState(parameters: PolicyParameters): ApplicationAllowlistState {
        return when (val result = getAllowedNamesUseCase()) {
            is ApiResult.Success -> configuration.fromAllowlistData(result.data)
            is ApiResult.Error -> defaultValue.copy(
                error = result.apiError,
                exception = result.exception
            )
            ApiResult.NotSupported -> defaultValue.copy(isSupported = false)
        }
    }

    override suspend fun setState(state: ApplicationAllowlistState): ApiResult<Unit> {
        val apiData = configuration.toApiData(state)

        return when {
            apiData.shouldClear -> {
                // Disable: Clear the entire allowlist
                when (val result = clearAllowlistUseCase()) {
                    is ApiResult.Success -> ApiResult.Success(Unit)
                    is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                    ApiResult.NotSupported -> ApiResult.NotSupported
                }
            }
            apiData.packageToAdd.isNotBlank() -> {
                // Enable with package: Add the package to the allowlist
                when (val result = addPackageUseCase(apiData.packageToAdd)) {
                    is ApiResult.Success -> ApiResult.Success(Unit)
                    is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
                    ApiResult.NotSupported -> ApiResult.NotSupported
                }
            }
            else -> {
                // Enable without package: No-op (allowlist already has packages or nothing to add)
                ApiResult.Success(Unit)
            }
        }
    }

    /**
     * Remove a specific package from the allowlist.
     *
     * This is a convenience method for removing individual packages without
     * clearing the entire list.
     *
     * @param packageName The package name to remove
     * @return Success if removed, Error otherwise
     */
    suspend fun removePackage(packageName: String): ApiResult<Unit> {
        return when (val result = removePackageUseCase(packageName)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
