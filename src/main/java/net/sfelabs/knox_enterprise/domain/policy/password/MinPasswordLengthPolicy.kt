package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiResult
import net.sfelabs.knox.core.feature.annotation.PolicyDefinition
import net.sfelabs.knox.core.feature.api.BooleanStatePolicy
import net.sfelabs.knox.core.feature.api.PolicyCapability
import net.sfelabs.knox.core.feature.api.PolicyCategory
import net.sfelabs.knox_enterprise.domain.use_cases.admin.GetActiveAdminComponentUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.GetMinPasswordLengthUseCase
import net.sfelabs.knox_enterprise.domain.use_cases.password.SetMinPasswordLengthUseCase

/**
 * Policy to enforce minimum password length.
 *
 * STIG V-268924 requires a minimum password length of 6 characters.
 * When enabled, sets the minimum to [STIG_MIN_LENGTH] (6).
 * When disabled, removes the restriction (sets to 0).
 */
@PolicyDefinition(
    title = "Minimum Password Length",
    description = "Enforce minimum password length per STIG V-268924. When enabled, requires at least 6 characters.",
    category = PolicyCategory.Toggle,
    capabilities = [
        PolicyCapability.MODIFIES_SECURITY,
        PolicyCapability.SECURITY_SENSITIVE,
        PolicyCapability.STIG
    ]
)
class MinPasswordLengthPolicy : BooleanStatePolicy() {

    private val getAdminUseCase = GetActiveAdminComponentUseCase()
    private val getUseCase = GetMinPasswordLengthUseCase()
    private val setUseCase = SetMinPasswordLengthUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> {
        // Get admin component
        val admin = when (val adminResult = getAdminUseCase()) {
            is ApiResult.Success -> adminResult.data
            is ApiResult.Error -> return ApiResult.Error(adminResult.apiError, adminResult.exception)
            ApiResult.NotSupported -> return ApiResult.NotSupported
        }

        return when (val result = getUseCase(admin)) {
            is ApiResult.Success -> {
                // Enabled if length meets STIG requirement (>= 6)
                val meetsRequirement = result.data >= STIG_MIN_LENGTH
                ApiResult.Success(meetsRequirement)
            }
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> {
        // Get admin component
        val admin = when (val adminResult = getAdminUseCase()) {
            is ApiResult.Success -> adminResult.data
            is ApiResult.Error -> return ApiResult.Error(adminResult.apiError, adminResult.exception)
            ApiResult.NotSupported -> return ApiResult.NotSupported
        }

        val params = SetMinPasswordLengthUseCase.Params(
            adminComponent = admin,
            minLength = if (enabled) STIG_MIN_LENGTH else DISABLED_VALUE
        )
        return when (val result = setUseCase(params)) {
            is ApiResult.Success -> ApiResult.Success(Unit)
            is ApiResult.Error -> ApiResult.Error(result.apiError, result.exception)
            ApiResult.NotSupported -> ApiResult.NotSupported
        }
    }

    companion object {
        /** STIG V-268924 requires minimum 6 characters */
        const val STIG_MIN_LENGTH = 6
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
