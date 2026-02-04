package net.sfelabs.knox_enterprise.domain.policy.application

import net.sfelabs.knox.core.feature.api.PolicyConfiguration
import net.sfelabs.knox.core.feature.api.StateMapping
import net.sfelabs.knox.core.feature.ui.model.ConfigurationOption

/**
 * Data class representing the allowlist API data for add/remove operations.
 *
 * @property packageToAdd Package name to add (empty string means no add operation)
 * @property shouldClear Whether to clear the entire allowlist
 */
data class AllowlistApiData(
    val packageToAdd: String,
    val shouldClear: Boolean
)

/**
 * Configuration for the application allowlist policy.
 *
 * Handles conversion between:
 * - API data (Set<String> for reading, AllowlistApiData for writing)
 * - UI state (toggle + text input for package name + list display)
 * - Policy state (ApplicationAllowlistState)
 */
data class ApplicationAllowlistConfiguration(
    override val stateMapping: StateMapping = StateMapping.DIRECT
) : PolicyConfiguration<ApplicationAllowlistState, AllowlistApiData> {

    /**
     * Convert API response (current allowlist) to policy state.
     * Note: This is called with the result of getState, not setState.
     * For this policy, we use a separate method to convert the Set<String> from the getter.
     */
    override fun fromApiData(apiData: AllowlistApiData): ApplicationAllowlistState {
        // This method is used for setter response, which doesn't return the list
        // The actual list is retrieved via fromAllowlistData
        return ApplicationAllowlistState(
            isEnabled = !apiData.shouldClear && apiData.packageToAdd.isNotEmpty(),
            allowedPackages = emptySet(),
            packageToAdd = apiData.packageToAdd
        )
    }

    /**
     * Convert the current allowlist (from getter use case) to policy state.
     */
    fun fromAllowlistData(packages: Set<String>): ApplicationAllowlistState {
        return ApplicationAllowlistState(
            isEnabled = packages.isNotEmpty(),
            allowedPackages = packages,
            packageToAdd = ""
        )
    }

    /**
     * Convert policy state to API data for the setter.
     */
    override fun toApiData(state: ApplicationAllowlistState): AllowlistApiData {
        return if (state.isEnabled) {
            AllowlistApiData(
                packageToAdd = state.packageToAdd,
                shouldClear = false
            )
        } else {
            AllowlistApiData(
                packageToAdd = "",
                shouldClear = true
            )
        }
    }

    override fun fromUiState(
        uiEnabled: Boolean,
        options: List<ConfigurationOption>
    ): ApplicationAllowlistState {
        val packageToAdd = options.filterIsInstance<ConfigurationOption.TextInput>()
            .find { it.key == KEY_PACKAGE_TO_ADD }
            ?.value ?: ""

        val allowedPackages = options.filterIsInstance<ConfigurationOption.TextList>()
            .find { it.key == KEY_ALLOWED_PACKAGES }
            ?.values ?: emptySet()

        return ApplicationAllowlistState(
            isEnabled = uiEnabled,
            allowedPackages = allowedPackages,
            packageToAdd = packageToAdd
        )
    }

    override fun getConfigurationOptions(state: ApplicationAllowlistState): List<ConfigurationOption> =
        listOf(
            ConfigurationOption.TextInput(
                key = KEY_PACKAGE_TO_ADD,
                label = "Package Name",
                value = state.packageToAdd,
                hint = "e.g., com.example.app"
            ),
            ConfigurationOption.TextList(
                key = KEY_ALLOWED_PACKAGES,
                label = "Allowed Packages",
                values = state.allowedPackages,
                hint = "Currently allowed packages"
            )
        )

    companion object {
        const val KEY_PACKAGE_TO_ADD = "packageToAdd"
        const val KEY_ALLOWED_PACKAGES = "allowedPackages"
    }
}
