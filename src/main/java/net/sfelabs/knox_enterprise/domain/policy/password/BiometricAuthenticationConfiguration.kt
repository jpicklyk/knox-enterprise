package net.sfelabs.knox_enterprise.domain.policy.password

import net.sfelabs.knox.core.feature.api.PolicyConfiguration
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox.core.feature.ui.model.ConfigurationOption
import net.sfelabs.knox_enterprise.domain.model.BiometricAuthType

/**
 * Data class representing the biometric authentication API data.
 */
data class BiometricAuthDto(
    val type: BiometricAuthType,
    val enabled: Boolean
)

data class BiometricAuthenticationConfiguration(
    override val stateMapping: StateMapping = StateMapping.DIRECT
) : PolicyConfiguration<BiometricAuthenticationState, BiometricAuthDto> {

    private val biometricOptions = listOf(
        "All Biometrics",
        "Fingerprint Only",
        "Face Only",
        "Iris Only"
    )

    override fun fromApiData(apiData: BiometricAuthDto): BiometricAuthenticationState {
        return BiometricAuthenticationState(
            isEnabled = apiData.enabled,
            biometricType = apiData.type
        )
    }

    override fun toApiData(state: BiometricAuthenticationState): BiometricAuthDto {
        return BiometricAuthDto(
            type = state.biometricType,
            enabled = state.isEnabled
        )
    }

    override fun fromUiState(
        uiEnabled: Boolean,
        options: List<ConfigurationOption>
    ): BiometricAuthenticationState {
        val selectedOption = options.filterIsInstance<ConfigurationOption.Choice>()
            .find { it.key == "biometricType" }
            ?.selected ?: "All Biometrics"

        val biometricType = when (selectedOption) {
            "Fingerprint Only" -> BiometricAuthType.FINGERPRINT
            "Face Only" -> BiometricAuthType.FACE
            "Iris Only" -> BiometricAuthType.IRIS
            else -> BiometricAuthType.ALL
        }

        return BiometricAuthenticationState(
            isEnabled = uiEnabled,
            biometricType = biometricType
        )
    }

    override fun getConfigurationOptions(state: BiometricAuthenticationState): List<ConfigurationOption> {
        val selectedOption = when (state.biometricType) {
            BiometricAuthType.FINGERPRINT -> "Fingerprint Only"
            BiometricAuthType.FACE -> "Face Only"
            BiometricAuthType.IRIS -> "Iris Only"
            BiometricAuthType.ALL -> "All Biometrics"
        }

        return listOf(
            ConfigurationOption.Choice(
                key = "biometricType",
                label = "Biometric Type",
                options = biometricOptions,
                selected = selectedOption
            )
        )
    }
}
