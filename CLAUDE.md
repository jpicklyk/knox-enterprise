# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Module Overview

knox-enterprise wraps Samsung Knox Enterprise SDK APIs as **suspend functions** with coroutine-friendly interfaces. All use cases return `ApiResult<T>` for unified error handling.

## Build Commands

```bash
./gradlew :knox-enterprise:build          # Build this module
./gradlew :knox-enterprise:test           # Run unit tests
./gradlew clean build                     # Full clean build
```

## Architecture

```
knox-enterprise/
├── domain/
│   ├── model/           # Domain models (CCModeState, CertificateType, UsbInterface)
│   ├── policy/          # Policy implementations organized by domain
│   │   ├── application/ # App allowlist policies
│   │   ├── audio/       # Volume panel policies
│   │   ├── browser/     # Browser settings policies
│   │   ├── connectivity/# WiFi, Bluetooth, NFC, mobile data
│   │   ├── datetime/    # Date/time policies
│   │   ├── device/      # Device settings (factory reset, backup, etc.)
│   │   ├── display/     # Screen brightness, backlight
│   │   ├── hardware/    # SD card, home key
│   │   ├── password/    # Password complexity policies (STIG)
│   │   ├── security/    # CC mode, encryption, lock screen
│   │   ├── system/      # USB mass storage, power menu
│   │   └── telephony/   # SMS, calls, SIM policies
│   └── use_cases/       # Knox SDK wrappers organized by domain
└── libs/
    └── knoxsdk_ver38.jar # Knox Enterprise SDK (compileOnly)
```

## Use Case Patterns

Use cases extend `SuspendingUseCase<P, R>` where `P` is the parameter type:

### No Parameters
```kotlin
class GetBrightnessValueUseCase : SuspendingUseCase<Unit, Int>()
```

### Single Parameter (use type directly)
```kotlin
class SetAdbStateUseCase : SuspendingUseCase<Boolean, Boolean>()
class SetBrightnessUseCase : SuspendingUseCase<Int, Unit>()
```

### Multiple Parameters (nested data class)
```kotlin
class SetAutoRotationStateUseCase : SuspendingUseCase<SetAutoRotationStateUseCase.Params, Unit>() {
    data class Params(val enabled: Boolean, val rotationMode: Int = 0)

    override suspend fun execute(params: Params): ApiResult<Unit> { ... }
}
```

### Parameter Naming
Always name the parameter `params` in `execute()`:
```kotlin
override suspend fun execute(params: Boolean): ApiResult<Unit>  // Correct
override suspend fun execute(enabled: Boolean): ApiResult<Unit> // Causes warning
```

## Policy System

Policies combine related use cases with metadata annotations. Three types exist:

### BooleanStatePolicy (Simple Toggle)
```kotlin
@PolicyDefinition(
    title = "Disable Volume Panel",
    description = "When enabled, disables the system volume panel.",
    category = PolicyCategory.Toggle,
    capabilities = [PolicyCapability.MODIFIES_AUDIO]
)
class VolumePanelPolicy : BooleanStatePolicy(StateMapping.INVERTED) {
    private val getUseCase = GetVolumePanelEnabledStateUseCase()
    private val setUseCase = SetVolumePanelEnabledStateUseCase()

    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
```

### StateMapping (Inverted Semantics)
- `StateMapping.DIRECT` - Policy "enabled" = API "enabled" (default)
- `StateMapping.INVERTED` - Policy "enabled" = API "disabled" (use when Knox API has inverted semantics)

### ConfigurableStatePolicy (Complex State)
For policies with configuration beyond simple on/off:
```kotlin
@PolicyDefinition(
    title = "Minimum Password Length",
    description = "Enforce minimum password length per STIG V-268924.",
    category = PolicyCategory.ConfigurableToggle,
    capabilities = [PolicyCapability.MODIFIES_SECURITY, PolicyCapability.STIG]
)
class MinPasswordLengthPolicy : ConfigurableStatePolicy<
    MinPasswordLengthState,    // UI state type
    Int,                       // API data type
    MinPasswordLengthConfiguration  // Configuration class
>(stateMapping = StateMapping.DIRECT) {
    override val configuration = MinPasswordLengthConfiguration(stateMapping = stateMapping)
    override val defaultValue = MinPasswordLengthState(isEnabled = false, minLength = 6)

    override suspend fun getState(parameters: PolicyParameters): MinPasswordLengthState { ... }
    override suspend fun setState(state: MinPasswordLengthState): ApiResult<Unit> { ... }
}
```

## Policy Naming Conventions

- **Allow policies**: Use "Allow" prefix when the policy permits something
  - `AllowAirplaneModePolicy` - When enabled, allows airplane mode changes
- **Enabled policies**: Use "Enabled" suffix for state toggles
  - `BluetoothEnabledPolicy` - Controls Bluetooth on/off state
- **Blocked policies**: Use "Blocked" suffix when policy blocks functionality
  - `AiCloudAppsBlockedPolicy` - When enabled, blocks AI cloud apps
- **Inverted policies**: Use `StateMapping.INVERTED` when Knox API has inverted logic
  - `VolumePanelPolicy(StateMapping.INVERTED)` - "Disable Volume Panel" policy

## Key Policy Capabilities

Annotate policies with relevant capabilities for filtering/grouping:

```kotlin
PolicyCapability.MODIFIES_SECURITY    // Affects device security
PolicyCapability.AFFECTS_CONNECTIVITY // Affects network/wireless
PolicyCapability.MODIFIES_AUDIO       // Affects sound
PolicyCapability.MODIFIES_DISPLAY     // Affects screen
PolicyCapability.SECURITY_SENSITIVE   // High-risk security change
PolicyCapability.STIG                 // STIG compliance requirement
PolicyCapability.REQUIRES_REBOOT      // Change requires device restart
```

## SDK Manager Access Pattern

Use cases access Knox managers via lazy initialization with `WithAndroidApplicationContext`:

```kotlin
class SetAdbStateUseCase : WithAndroidApplicationContext, SuspendingUseCase<Boolean, Boolean>() {
    private val restrictionPolicy by lazy {
        EnterpriseDeviceManager.getInstance(applicationContext).restrictionPolicy
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return try {
            restrictionPolicy.setAdbEnabled(params)
            ApiResult.Success(params)
        } catch (e: SecurityException) {
            ApiResult.Error(DefaultApiError.PermissionError(e.message ?: "Permission denied"))
        }
    }
}
```

## Error Handling

All use cases return `ApiResult<T>`:

```kotlin
sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val apiError: ApiError, val exception: Exception? = null) : ApiResult<Nothing>()
    data object NotSupported : ApiResult<Nothing>()
}
```

Handle results with when:
```kotlin
when (val result = useCase(params)) {
    is ApiResult.Success -> handleSuccess(result.data)
    is ApiResult.Error -> handleError(result.apiError.message)
    ApiResult.NotSupported -> handleNotSupported()
}
```

## Generated Code (KSP)

The `@PolicyDefinition` annotation generates:
- `GeneratedPolicyComponents` - Registry with `getAll()` returning all policy components
- `*Component` - `PolicyComponent<T>` for each policy
- `*Key` - Type-safe registry lookup keys
- `PolicyType` - Sealed interface for exhaustive pattern matching

Generated code location: `build/generated/ksp/*/kotlin/`

## File Naming

- Use cases: `[Action][Entity]UseCase.kt` (e.g., `SetAdbStateUseCase.kt`)
- Policies: `[Feature]Policy.kt` (e.g., `ApplicationAllowlistPolicy.kt`)
- State classes: `[Feature]State.kt` + `[Feature]Configuration.kt`

## Testing

This project uses **MockK** (not Mockito):

```kotlin
@Test
fun `test brightness change`() = runTest {
    val mockUseCase = mockk<SetBrightnessUseCase>()
    coEvery { mockUseCase.invoke(any()) } returns ApiResult.Success(Unit)
    // Test your code
}
```

## Dependencies

This module is DI-agnostic. For Hilt integration, use the `knox-hilt` module which provides the DI bindings.

```kotlin
// Direct instantiation (no DI)
val useCase = SetAdbStateUseCase()
val result = useCase(true)

// With Hilt via knox-hilt module
@Inject lateinit var useCase: SetAdbStateUseCase
```
