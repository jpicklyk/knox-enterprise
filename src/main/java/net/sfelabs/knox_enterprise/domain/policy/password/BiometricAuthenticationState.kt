package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.domain.usecase.model.ApiError
import net.sfelabs.knox.core.feature.api.PolicyState
import net.sfelabs.knox_enterprise.domain.model.BiometricAuthType

data class BiometricAuthenticationState(
    override val isEnabled: Boolean,
    override val isSupported: Boolean = true,
    override val error: ApiError? = null,
    override val exception: Throwable? = null,
    val biometricType: BiometricAuthType = BiometricAuthType.ALL
) : PolicyState {
    override fun withEnabled(enabled: Boolean): PolicyState = copy(isEnabled = enabled)

    override fun withError(error: ApiError?, exception: Throwable?): PolicyState =
        copy(error = error, exception = exception)
}
