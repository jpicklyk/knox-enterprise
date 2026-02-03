package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiError
import net.sfelabs.knox.core.feature.api.PolicyState

data class MinPasswordLengthState(
    override val isEnabled: Boolean,
    override val isSupported: Boolean = true,
    override val error: ApiError? = null,
    override val exception: Throwable? = null,
    val minLength: Int = DEFAULT_MIN_LENGTH
) : PolicyState {
    override fun withEnabled(enabled: Boolean): PolicyState = copy(isEnabled = enabled)

    override fun withError(error: ApiError?, exception: Throwable?): PolicyState =
        copy(error = error, exception = exception)

    companion object {
        /** STIG V-268924 requires minimum 6 characters */
        const val DEFAULT_MIN_LENGTH = 6
        /** Minimum valid password length */
        const val MIN_LENGTH = 1
        /** Maximum valid password length */
        const val MAX_LENGTH = 32
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
