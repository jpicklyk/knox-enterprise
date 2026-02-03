package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiError
import net.sfelabs.knox.core.feature.api.PolicyState

data class MaxFailedPasswordsState(
    override val isEnabled: Boolean,
    override val isSupported: Boolean = true,
    override val error: ApiError? = null,
    override val exception: Throwable? = null,
    val maxAttempts: Int = DEFAULT_MAX_ATTEMPTS
) : PolicyState {
    override fun withEnabled(enabled: Boolean): PolicyState = copy(isEnabled = enabled)

    override fun withError(error: ApiError?, exception: Throwable?): PolicyState =
        copy(error = error, exception = exception)

    companion object {
        /** STIG V-268928 requires max 10 failed attempts */
        const val DEFAULT_MAX_ATTEMPTS = 10
        /** Minimum valid limit */
        const val MIN_ATTEMPTS = 1
        /** Maximum valid limit */
        const val MAX_ATTEMPTS = 30
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
