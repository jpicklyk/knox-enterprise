package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.feature.api.PolicyConfiguration
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox.core.feature.ui.model.ConfigurationOption

data class MinPasswordLengthConfiguration(
    override val stateMapping: StateMapping = StateMapping.DIRECT
) : PolicyConfiguration<MinPasswordLengthState, Int> {

    override fun fromApiData(apiData: Int): MinPasswordLengthState {
        val isEnabled = apiData >= MinPasswordLengthState.MIN_LENGTH
        return MinPasswordLengthState(
            isEnabled = isEnabled,
            minLength = if (isEnabled) apiData else MinPasswordLengthState.DEFAULT_MIN_LENGTH
        )
    }

    override fun toApiData(state: MinPasswordLengthState): Int {
        return if (state.isEnabled) state.minLength else MinPasswordLengthState.DISABLED_VALUE
    }

    override fun fromUiState(
        uiEnabled: Boolean,
        options: List<ConfigurationOption>
    ): MinPasswordLengthState {
        val minLength = options.filterIsInstance<ConfigurationOption.NumberInput>()
            .find { it.key == "minLength" }
            ?.value ?: MinPasswordLengthState.DEFAULT_MIN_LENGTH

        return MinPasswordLengthState(
            isEnabled = uiEnabled,
            minLength = minLength
        )
    }

    override fun getConfigurationOptions(state: MinPasswordLengthState): List<ConfigurationOption> = listOf(
        ConfigurationOption.NumberInput(
            key = "minLength",
            label = "Minimum Length",
            value = state.minLength,
            range = MinPasswordLengthState.MIN_LENGTH..MinPasswordLengthState.MAX_LENGTH
        )
    )
}
