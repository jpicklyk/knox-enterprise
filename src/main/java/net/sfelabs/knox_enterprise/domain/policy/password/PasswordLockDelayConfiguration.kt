package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.feature.api.PolicyConfiguration
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox.core.feature.ui.model.ConfigurationOption

data class PasswordLockDelayConfiguration(
    override val stateMapping: StateMapping = StateMapping.DIRECT
) : PolicyConfiguration<PasswordLockDelayState, Int> {

    override fun fromApiData(apiData: Int): PasswordLockDelayState {
        // Enabled if delay is within STIG-compliant range (0-5 seconds)
        val isEnabled = apiData in PasswordLockDelayState.MIN_DELAY..PasswordLockDelayState.DEFAULT_DELAY_SECONDS
        return PasswordLockDelayState(
            isEnabled = isEnabled,
            delaySeconds = apiData.coerceIn(PasswordLockDelayState.MIN_DELAY, PasswordLockDelayState.MAX_DELAY)
        )
    }

    override fun toApiData(state: PasswordLockDelayState): Int {
        return if (state.isEnabled) state.delaySeconds else PasswordLockDelayState.DISABLED_VALUE
    }

    override fun fromUiState(
        uiEnabled: Boolean,
        options: List<ConfigurationOption>
    ): PasswordLockDelayState {
        val delaySeconds = options.filterIsInstance<ConfigurationOption.NumberInput>()
            .find { it.key == "delaySeconds" }
            ?.value ?: PasswordLockDelayState.DEFAULT_DELAY_SECONDS

        return PasswordLockDelayState(
            isEnabled = uiEnabled,
            delaySeconds = delaySeconds
        )
    }

    override fun getConfigurationOptions(state: PasswordLockDelayState): List<ConfigurationOption> = listOf(
        ConfigurationOption.NumberInput(
            key = "delaySeconds",
            label = "Delay (seconds)",
            value = state.delaySeconds,
            range = PasswordLockDelayState.MIN_DELAY..PasswordLockDelayState.MAX_DELAY
        )
    )
}
