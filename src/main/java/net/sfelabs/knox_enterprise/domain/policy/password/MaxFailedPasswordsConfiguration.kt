package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.feature.api.PolicyConfiguration
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox.core.feature.ui.model.ConfigurationOption

data class MaxFailedPasswordsConfiguration(
    override val stateMapping: StateMapping = StateMapping.DIRECT
) : PolicyConfiguration<MaxFailedPasswordsState, Int> {

    override fun fromApiData(apiData: Int): MaxFailedPasswordsState {
        val isEnabled = apiData in MaxFailedPasswordsState.MIN_ATTEMPTS..MaxFailedPasswordsState.MAX_ATTEMPTS
        return MaxFailedPasswordsState(
            isEnabled = isEnabled,
            maxAttempts = if (isEnabled) apiData else MaxFailedPasswordsState.DEFAULT_MAX_ATTEMPTS
        )
    }

    override fun toApiData(state: MaxFailedPasswordsState): Int {
        return if (state.isEnabled) state.maxAttempts else MaxFailedPasswordsState.DISABLED_VALUE
    }

    override fun fromUiState(
        uiEnabled: Boolean,
        options: List<ConfigurationOption>
    ): MaxFailedPasswordsState {
        val maxAttempts = options.filterIsInstance<ConfigurationOption.NumberInput>()
            .find { it.key == "maxAttempts" }
            ?.value ?: MaxFailedPasswordsState.DEFAULT_MAX_ATTEMPTS

        return MaxFailedPasswordsState(
            isEnabled = uiEnabled,
            maxAttempts = maxAttempts
        )
    }

    override fun getConfigurationOptions(state: MaxFailedPasswordsState): List<ConfigurationOption> = listOf(
        ConfigurationOption.NumberInput(
            key = "maxAttempts",
            label = "Max Attempts",
            value = state.maxAttempts,
            range = MaxFailedPasswordsState.MIN_ATTEMPTS..MaxFailedPasswordsState.MAX_ATTEMPTS
        )
    )
}
