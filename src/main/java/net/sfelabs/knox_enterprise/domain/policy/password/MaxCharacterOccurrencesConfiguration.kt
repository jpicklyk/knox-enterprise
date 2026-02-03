package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.feature.api.PolicyConfiguration
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox.core.feature.ui.model.ConfigurationOption

data class MaxCharacterOccurrencesConfiguration(
    override val stateMapping: StateMapping = StateMapping.DIRECT
) : PolicyConfiguration<MaxCharacterOccurrencesState, Int> {

    override fun fromApiData(apiData: Int): MaxCharacterOccurrencesState {
        val isEnabled = apiData in MaxCharacterOccurrencesState.MIN_OCCURRENCES..MaxCharacterOccurrencesState.MAX_OCCURRENCES
        return MaxCharacterOccurrencesState(
            isEnabled = isEnabled,
            maxOccurrences = if (isEnabled) apiData else MaxCharacterOccurrencesState.DEFAULT_MAX_OCCURRENCES
        )
    }

    override fun toApiData(state: MaxCharacterOccurrencesState): Int {
        return if (state.isEnabled) state.maxOccurrences else MaxCharacterOccurrencesState.DISABLED_VALUE
    }

    override fun fromUiState(
        uiEnabled: Boolean,
        options: List<ConfigurationOption>
    ): MaxCharacterOccurrencesState {
        val maxOccurrences = options.filterIsInstance<ConfigurationOption.NumberInput>()
            .find { it.key == "maxOccurrences" }
            ?.value ?: MaxCharacterOccurrencesState.DEFAULT_MAX_OCCURRENCES

        return MaxCharacterOccurrencesState(
            isEnabled = uiEnabled,
            maxOccurrences = maxOccurrences
        )
    }

    override fun getConfigurationOptions(state: MaxCharacterOccurrencesState): List<ConfigurationOption> = listOf(
        ConfigurationOption.NumberInput(
            key = "maxOccurrences",
            label = "Max Occurrences",
            value = state.maxOccurrences,
            range = MaxCharacterOccurrencesState.MIN_OCCURRENCES..MaxCharacterOccurrencesState.MAX_OCCURRENCES
        )
    )
}
