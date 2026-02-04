package net.sfelabs.knox_enterprise.domain.policy.application

import net.sfelabs.knox.core.domain.usecase.model.ApiError
import net.sfelabs.knox.core.feature.api.PolicyState

/**
 * State for the application allowlist policy.
 *
 * @property isEnabled True if the allowlist is active (contains packages)
 * @property allowedPackages The set of package names currently in the allowlist
 * @property packageToAdd The package name to add when enabling (used for UI input)
 */
data class ApplicationAllowlistState(
    override val isEnabled: Boolean,
    override val isSupported: Boolean = true,
    override val error: ApiError? = null,
    override val exception: Throwable? = null,
    val allowedPackages: Set<String> = emptySet(),
    val packageToAdd: String = ""
) : PolicyState {
    override fun withEnabled(enabled: Boolean): PolicyState = copy(isEnabled = enabled)

    override fun withError(error: ApiError?, exception: Throwable?): PolicyState =
        copy(error = error, exception = exception)
}
