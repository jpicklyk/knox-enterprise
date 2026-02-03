package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.feature.api.PolicyConfiguration
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox.core.feature.ui.model.ConfigurationOption

/**
 * Shared configuration for both character and numeric sequence length policies.
 */
data class MaxSequenceLengthConfiguration(
    override val stateMapping: StateMapping = StateMapping.DIRECT,
    private val optionLabel: String = "Max Sequence Length"
) : PolicyConfiguration<MaxSequenceLengthState, Int> {

    override fun fromApiData(apiData: Int): MaxSequenceLengthState {
        val isEnabled = apiData in MaxSequenceLengthState.MIN_SEQUENCE..MaxSequenceLengthState.MAX_SEQUENCE
        return MaxSequenceLengthState(
            isEnabled = isEnabled,
            maxSequence = if (isEnabled) apiData else MaxSequenceLengthState.DEFAULT_MAX_SEQUENCE
        )
    }

    override fun toApiData(state: MaxSequenceLengthState): Int {
        return if (state.isEnabled) state.maxSequence else MaxSequenceLengthState.DISABLED_VALUE
    }

    override fun fromUiState(
        uiEnabled: Boolean,
        options: List<ConfigurationOption>
    ): MaxSequenceLengthState {
        val maxSequence = options.filterIsInstance<ConfigurationOption.NumberInput>()
            .find { it.key == "maxSequence" }
            ?.value ?: MaxSequenceLengthState.DEFAULT_MAX_SEQUENCE

        return MaxSequenceLengthState(
            isEnabled = uiEnabled,
            maxSequence = maxSequence
        )
    }

    override fun getConfigurationOptions(state: MaxSequenceLengthState): List<ConfigurationOption> = listOf(
        ConfigurationOption.NumberInput(
            key = "maxSequence",
            label = optionLabel,
            value = state.maxSequence,
            range = MaxSequenceLengthState.MIN_SEQUENCE..MaxSequenceLengthState.MAX_SEQUENCE
        )
    )
}
