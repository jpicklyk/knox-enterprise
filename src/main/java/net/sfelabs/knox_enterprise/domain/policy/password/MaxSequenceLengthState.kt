package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiError
import net.sfelabs.knox.core.feature.api.PolicyState

/**
 * Shared state for both character and numeric sequence length policies.
 */
data class MaxSequenceLengthState(
    override val isEnabled: Boolean,
    override val isSupported: Boolean = true,
    override val error: ApiError? = null,
    override val exception: Throwable? = null,
    val maxSequence: Int = DEFAULT_MAX_SEQUENCE
) : PolicyState {
    override fun withEnabled(enabled: Boolean): PolicyState = copy(isEnabled = enabled)

    override fun withError(error: ApiError?, exception: Throwable?): PolicyState =
        copy(error = error, exception = exception)

    companion object {
        /** STIG V-268925 requires max 3 sequential characters/digits */
        const val DEFAULT_MAX_SEQUENCE = 3
        /** Minimum valid sequence limit */
        const val MIN_SEQUENCE = 1
        /** Maximum valid sequence limit */
        const val MAX_SEQUENCE = 10
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
