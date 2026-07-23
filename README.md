# knox-enterprise

Android library module for Samsung Knox Enterprise SDK integration. This module provides coroutine-based use cases for enterprise device management features available on Samsung devices with Knox support.

## Overview

knox-enterprise wraps the Samsung Knox Enterprise SDK APIs as **suspend functions**, providing a clean coroutine-friendly API for standard enterprise device management features. Rather than duplicating the Knox SDK documentation, this module focuses on making the SDK easier to use within modern Kotlin coroutine-based Android applications.

**Key Design Principles:**
- Each use case is a thin coroutine wrapper around a Knox SDK API call
- Use cases extend `SuspendingUseCase<P, R>` for consistent async execution
- All operations return `ApiResult<T>` for unified error handling
- The module is DI-agnostic and can be used with any dependency injection framework

## Use Case Architecture

### Coroutine Wrappers

Each use case is a coroutine wrapper around Knox SDK APIs. The use cases:

1. **Encapsulate SDK access** - All Knox SDK manager access is internal to the use case
2. **Provide suspend functions** - Use cases can be called from coroutines or `runBlocking`
3. **Return consistent results** - All use cases return `ApiResult<T>` for success/error handling
4. **Handle SDK errors** - Knox error codes are mapped to meaningful error messages

### Use Case Patterns

Use cases extend `SuspendingUseCase<P, R>` where `P` is the parameter type and `R` is the return type:

```kotlin
// No parameters - use Unit
class GetBrightnessValueUseCase : SuspendingUseCase<Unit, Int>()

// Single parameter - use the type directly
class SetVolumeControlStreamUseCase : SuspendingUseCase<Int, Unit>()
class SetAdbStateUseCase : SuspendingUseCase<Boolean, Boolean>()

// Multiple parameters - use a nested data class
class SetAutoRotationStateUseCase : SuspendingUseCase<SetAutoRotationStateUseCase.Params, Unit>() {
    data class Params(val enabled: Boolean, val rotationMode: Int = 0)
}
```

### SDK Manager Access

Use cases access Knox SDK managers via lazy initialization:

```kotlin
class SetAdbStateUseCase : SuspendingUseCase<Boolean, Boolean>() {
    // Lazy initialization of the Knox manager
    private val settingsManager by lazy {
        CustomDeviceManager.getInstance().settingsManager
    }

    override suspend fun execute(params: Boolean): ApiResult<Boolean> {
        return settingsManager.setAdbState(params).toKnoxApiResult("setAdbState") { params }
    }
}
```

Knox `CustomDeviceManager` APIs report success/failure as `int` status codes. Instead of a
hand-rolled `when` in each use case, the module maps them centrally through an internal
`Int.toKnoxApiResult(operation)` extension, producing typed results:

```kotlin
// SUCCESS                 -> ApiResult.Success
// ERROR_NOT_SUPPORTED     -> ApiResult.NotSupported
// ERROR_POLICY_RESTRICTED -> ApiResult.Error(DefaultApiError.PolicyRestricted)
// ERROR_INVALID_VALUE     -> ApiResult.Error(DefaultApiError.InvalidInput)
return systemManager.setSomeState(params).toKnoxApiResult("setSomeState")
```

## Features

### Device Security

- **ADB Control** - Enable/disable Android Debug Bridge
- **CC Mode** - Common Criteria mode configuration
- **Firmware Recovery** - Allow/block firmware recovery mode
- **OTA Updates** - Control over-the-air update policy

### Display & Settings

- **Brightness Control** - Get/set screen brightness value and mode
- **Screen Settings** - Various display configuration options

### Connectivity & Data

- **Mobile Data** - Enable/disable mobile data
- **Data Roaming** - Control mobile data roaming
- **USB Host Storage** - Allow/block USB storage access
- **USB Exception List** - Configure USB device whitelist

### Certificates & Security

- **CA Certificate Installation** - Install trusted CA certificates
- **Certificate Management** - Manage device certificates by type and keystore

### Attestation

- **Device Attestation** - Generate attestation blobs
- **Key Generation** - Generate attestation keys
- **Attestation Support Check** - Verify device attestation capabilities

### Audit & Logging

- **Audit Log** - Enable/disable and query audit logging

## Architecture

knox-enterprise follows a DI-agnostic architecture:

```
┌─────────────────────────────────────────────────────────┐
│  Your App                                               │
├─────────────────────────────────────────────────────────┤
│  knox-hilt (optional - for Hilt users)                  │
├─────────────────────────────────────────────────────────┤
│  knox-enterprise (this module)                          │
│  ├── domain/use_cases/  - Business logic                │
│  ├── domain/policy/     - Policy implementations        │
│  └── generated/         - KSP-generated components      │
├─────────────────────────────────────────────────────────┤
│  knox-core              - Shared infrastructure         │
└─────────────────────────────────────────────────────────┘
```

### Generated Code

The `feature-processor` KSP plugin generates DI-agnostic code from `@PolicyDefinition` annotations:

- **`GeneratedPolicyComponents`** - Registry object with `getAll()` returning all policy components
- **`*Component`** - `PolicyComponent<T>` implementations for each policy
- **`*Key`** - Type-safe policy keys for registry lookup
- **`PolicyType`** - Sealed interface for exhaustive pattern matching

The generated code has **no DI framework dependencies**. DI integration is handled by separate modules (e.g., knox-hilt for Hilt users).

## Usage

### Direct Use Case Instantiation

Use cases can be instantiated directly without dependency injection:

```kotlin
// Control ADB state
val result = SetAdbStateUseCase().invoke(enabled = true)
when (result) {
    is ApiResult.Success -> println("ADB state changed")
    is ApiResult.Error -> println("Error: ${result.apiError.message}")
}

// Get screen brightness
val brightness = GetBrightnessValueUseCase().invoke(Unit)

// Set screen brightness
val result = SetBrightnessUseCase().invoke(enable = true, level = 128)

// Check CC Mode status
val ccMode = GetCCModeUseCase().invoke(Unit)

// Control mobile data
val result = SetMobileDataStateUseCase().invoke(enabled = true)

// Install CA certificate
val result = InstallCaCertificateUseCase().invoke(
    keystore = TargetKeystore.Default,
    certificateType = CertificateType.Cert,
    data = certBytes,
    alias = "my-ca-cert",
    password = ""
)
```

### Device Attestation

```kotlin
// Check if attestation is supported
val isSupported = IsAttestationSupportedUseCase().invoke(Unit)

// Generate attestation key
val keyResult = KeyGeneratorUseCase().invoke(Unit)

// Get attestation blob
val blobResult = GetAttestationBlobUseCase().invoke("attestation-challenge")
```

### With Hilt (via knox-hilt)

If using Dagger Hilt, add the knox-hilt module for automatic DI integration:

```kotlin
// In your app's build.gradle.kts
dependencies {
    implementation(project(":knox-enterprise"))
    implementation(project(":knox-hilt"))  // For Hilt integration
}
```

## Domain Models

knox-enterprise exposes domain models to avoid SDK dependencies in consuming code:

```kotlin
// CC Mode states (Int constants mirroring Knox AdvancedRestrictionPolicy)
object CCModeState {
    const val READY = 2
    const val ENABLED = 4
    const val UNKNOWN = -1
}

// Certificate types
sealed class CertificateType(val type: String) {
    data object Cert : CertificateType("CERT")
    data object Pkcs12 : CertificateType("PKCS12")
}

// Target keystore
sealed class TargetKeystore(val value: Int) {
    data object VpnAndApps : TargetKeystore(4)
    data object Wifi : TargetKeystore(2)
    data object Default : TargetKeystore(1)
}

// USB interface class bitmask constants (for the USB exception list)
object UsbInterface {
    const val OFF = 0   // allow all
    const val CDC = 1   // Communications Device Class (ethernet dongles)
    const val MAS = 2   // Mass Storage
    const val HID = 4   // Human Interface Device
    const val AUD = 8   // Audio
    const val VID = 16  // Video
    const val PRT = 32  // Printer
    const val WIR = 64  // Wireless controller
    const val MIS = 128 // Miscellaneous
    const val APP = 256 // Application specific
    const val VEN = 512 // Vendor specific
}
```

## Use Case Categories

Use cases are organized by Knox SDK policy domain. For detailed API documentation, refer to the [Samsung Knox SDK Documentation](https://docs.samsungknox.com/dev/knox-sdk/index.htm).

| Category | Package | Description |
|----------|---------|-------------|
| **Restrictions** | `adb/`, `system/`, `device/` | Device restrictions (ADB, USB mass storage, factory reset, etc.) |
| **Connectivity** | `connectivity/` | Network controls (WiFi, Bluetooth, NFC, mobile data) |
| **Telephony** | `telephony/` | Phone restrictions (SMS, calls, SIM PIN) |
| **Display** | `display/` | Screen settings (brightness, backlight, rotation) |
| **System** | `system/` | Hardware controls (speaker, USB, power menu) |
| **DateTime** | `datetime/` | Date/time settings and restrictions |
| **Browser** | `browser/` | Samsung browser policies (cookies, JavaScript, proxy) |
| **Firewall** | `firewall/` | Knox firewall and domain filtering |
| **Application** | `application/` | App management (install, enable, disable) |
| **Audio** | `audio/` | Volume and sound controls |
| **Status Bar** | `statusbar/` | Status bar and quick panel customization |
| **Security** | `security/` | Security policies (encryption, CC mode, firmware) |
| **Attestation** | `attestation/` | Device attestation and key generation |
| **Audit** | `audit/` | Audit logging |

### Policies

In addition to use cases, the module includes **Policy** classes that combine related get/set use cases with metadata annotations:

```kotlin
@PolicyDefinition(
    title = "ADB Enabled",
    description = "Enable or disable Android Debug Bridge access.",
    category = PolicyCategory.Toggle,
    capabilities = [PolicyCapability.MODIFIES_SECURITY]
)
class AdbEnabledPolicy : BooleanStatePolicy() {
    override suspend fun getEnabled(): ApiResult<Boolean> = getUseCase()
    override suspend fun setEnabled(enabled: Boolean): ApiResult<Unit> = setUseCase(enabled)
}
```

Policies are useful for building dynamic settings UIs where you need metadata about each setting.

## Knox SDK Documentation

This module does not duplicate the Knox SDK API documentation. For details on:
- What each API does
- Required permissions
- Supported device models
- Parameter values and meanings

Refer to the official Samsung Knox SDK documentation:
- [Knox SDK API Reference](https://docs.samsungknox.com/dev/knox-sdk/api-reference.htm)
- [Knox Platform for Enterprise](https://docs.samsungknox.com/dev/knox-sdk/index.htm)

## Requirements

- Samsung device with Knox Enterprise SDK support
- Knox Enterprise license activated
- Device Owner or appropriate Knox permissions

## SDK Isolation

The Knox Enterprise SDK JAR is included in the `libs/` directory:
- SDK classes are available at compile time
- All SDK access is encapsulated within use cases
- Consuming apps interact only with domain models and use cases

## Error Handling

All use cases return `ApiResult` which is a sealed class:

```kotlin
sealed class ApiResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : ApiResult<T>()
    data class Error(val apiError: ApiError, val exception: Exception? = null) : ApiResult<Nothing>()
    data object NotSupported : ApiResult<Nothing>()
}
```

Handle results appropriately:

```kotlin
when (val result = someUseCase.invoke(params)) {
    is ApiResult.Success -> {
        // Handle success with result.data
    }
    is ApiResult.Error -> {
        // Handle error with result.apiError.message
    }
    ApiResult.NotSupported -> {
        // Handle an API the device doesn't support
    }
}
```

## Testing

Use cases are designed for easy testing:

```kotlin
class MyViewModelTest {
    @Test
    fun `test brightness change`() = runTest {
        // Use cases can be mocked or stubbed
        val mockUseCase = mockk<SetBrightnessUseCase>()
        coEvery { mockUseCase.invoke(any()) } returns ApiResult.Success(Unit)

        // Test your ViewModel or other code
    }
}
```
