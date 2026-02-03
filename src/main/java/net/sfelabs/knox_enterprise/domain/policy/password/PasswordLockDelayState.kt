package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiError
import net.sfelabs.knox.core.feature.api.PolicyState

data class PasswordLockDelayState(
    override val isEnabled: Boolean,
    override val isSupported: Boolean = true,
    override val error: ApiError? = null,
    override val exception: Throwable? = null,
    val delaySeconds: Int = DEFAULT_DELAY_SECONDS
) : PolicyState {
    override fun withEnabled(enabled: Boolean): PolicyState = copy(isEnabled = enabled)

    override fun withError(error: ApiError?, exception: Throwable?): PolicyState =
        copy(error = error, exception = exception)

    companion object {
        /** STIG V-268926 requires lock within 5 seconds */
        const val DEFAULT_DELAY_SECONDS = 5
        /** Minimum delay (immediate lock) */
        const val MIN_DELAY = 0
        /** Maximum delay (5 minutes) */
        const val MAX_DELAY = 300
        /** Permissive value when disabled - 5 minutes */
        const val DISABLED_VALUE = 300
    }
}
