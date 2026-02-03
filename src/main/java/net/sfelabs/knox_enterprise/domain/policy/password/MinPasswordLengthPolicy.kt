package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.ConfigurableStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox.core.feature.api.PolicyParameters
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox_enterprise.domain.use_cases.admin.GetActiveAdminComponentUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMinPasswordLengthUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMinPasswordLengthUseCase

/**
 * Policy to enforce minimum password length.
 *
 * STIG V-268924 requires a minimum password length of 6 characters.
 * When enabled, enforces the configured minimum length.
 * When disabled, removes the restriction (sets to 0).
 */
@PolicyDefinition(
    title = "Minimum Password Length",
    description = "Enforce minimum password length per STIG V-268924. Configure the required minimum characters.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MinPasswordLengthPolicy : ConfigurableStatePolicy<
    MinPasswordLengthState,
    Int,
    MinPasswordLengthConfiguration
>(stateMapping = StateMapping.DIRECT) {

    private val getAdminUseCase = GetActiveAdminComponentUseCase()
    private val getUseCase = GetMinPasswordLengthUseCase()
    private val setUseCase = SetMinPasswordLengthUseCase()

    override val configuration = MinPasswordLengthConfiguration(stateMapping = stateMapping)

    override val defaultValue = MinPasswordLengthState(
        isEnabled = false,
        minLength = MinPasswordLengthState.DEFAULT_MIN_LENGTH
    )

    override suspend fun getState(parameters: PolicyParameters): MinPasswordLengthState {
        // Get admin component
        val admin = when (val adminResult = getAdminUseCase()) {
            is ApiResult.Success -> adminResult.data
            is ApiResult.Error -> return defaultValue.copy(
                error = adminResult.apiError,
                exception = adminResult.exception
            )
            ApiResult.NotSupported -> return defaultValue.copy(isSupported = false)
        }

        return when (val result = getUseCase(admin)) {
            is ApiResult.Success -> configuration.fromApiData(result.data)
            is ApiResult.Error -> defaultValue.copy(
                error = result.apiError,
                exception = result.exception
            )
            ApiResult.NotSupported -> defaultValue.copy(isSupported = false)
        }
    }

    override suspend fun setState(state: MinPasswordLengthState): ApiResult<Unit> {
        // Get admin component
        val admin = when (val adminResult = getAdminUseCase()) {
            is ApiResult.Success -> adminResult.data
            is ApiResult.Error -> return ApiResult.Error(adminResult.apiError, adminResult.exception)
            ApiResult.NotSupported -> return ApiResult.NotSupported
        }

        val params = SetMinPasswordLengthUseCase.Params(
            adminComponent = admin,
            minLength = configuration.toApiData(state)
        )

        return when (val result = setUseCase(params)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }
}
