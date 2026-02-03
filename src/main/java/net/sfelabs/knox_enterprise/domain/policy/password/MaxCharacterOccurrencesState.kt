package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiError
import net.sfelabs.knox.core.feature.api.PolicyState

data class MaxCharacterOccurrencesState(
    override val isEnabled: Boolean,
    override val isSupported: Boolean = true,
    override val error: ApiError? = null,
    override val exception: Throwable? = null,
    val maxOccurrences: Int = DEFAULT_MAX_OCCURRENCES
) : PolicyState {
    override fun withEnabled(enabled: Boolean): PolicyState = copy(isEnabled = enabled)

    override fun withError(error: ApiError?, exception: Throwable?): PolicyState =
        copy(error = error, exception = exception)

    companion object {
        /** STIG V-268925 requires max 4 occurrences of any character */
        const val DEFAULT_MAX_OCCURRENCES = 4
        /** Minimum valid limit */
        const val MIN_OCCURRENCES = 1
        /** Maximum valid limit */
        const val MAX_OCCURRENCES = 10
        /** Value of 0 disables the restriction */
        const val DISABLED_VALUE = 0
    }
}
